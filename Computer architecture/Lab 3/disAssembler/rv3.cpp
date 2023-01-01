#include <filesystem>
#include <iostream>
#include <fstream>
#include <cstring>
#include <vector>
#include <cstdio>
#include <array>
#include <map>

#include "elf_data.h"

std::array<std::string, 32> reg_names = {
        "zero",
        "ra",
        "sp",
        "gp",
        "tp",
        "t0",
        "t1",
        "t2",
        "s0",
        "s1",
        "a0",
        "a1",
        "a2",
        "a3",
        "a4",
        "a5",
        "a6",
        "a7",
        "s2",
        "s3",
        "s4",
        "s5",
        "s6",
        "s7",
        "s8",
        "s9",
        "s10",
        "s11",
        "t3",
        "t4",
        "t5",
        "t6"
};

constexpr unsigned int rd = 0xF80;
constexpr unsigned int rs1 = 0xF8000;
constexpr unsigned int rs2 = 0x1F00000;

std::map<Elf32_Addr, std::string> labels;
unsigned int cnt = 0;
FILE* out;

std::string get_string(Elf32_Word index, char* buffer) {
    size_t i = 0;
    while (*(buffer + index + i) != '\0') {
        ++i;
    }
    return std::string(buffer + index, i);
}

//parse symbtab element type
inline std::string get_symtab_elem_type(unsigned char info) {
    switch(info & 0xf) {
        case 0: return "NOTYPE";
        case 1: return "OBJECT";
        case 2: return "FUNC";
        case 3: return "SECTION";
        case 4: return "FILE";
        case 13: return "LOPROC";
        case 15: return "HIPROC";
    }
}

//parse symbtab element bind
inline std::string get_symtab_elem_bind(unsigned char info) {
    switch(info >> 4) {
        case 0: return "LOCAL";
        case 1: return "GLOBAL";
        case 2: return "WEAK";
        case 13: return "LOPROC";
        case 15: return "HIPROC";
    }
}

//parse symbtab element visibility
inline std::string get_symtab_elem_vis(unsigned char other) {
    switch(other & 0x3) {
        case 0: return "DEFAULT";
        case 1: return "INTERNAL";
        case 2: return "HIDDEN";
        case 3: return "PROTECTED";
    }
}

//parse symbtab element index
inline std::string get_symtab_elem_indx(
        unsigned int shndx,
        std::vector <Elf32_Shdr*>& elf_sections,
        char* &buffer,
        unsigned int tab_start
    ) {
    if (shndx >= elf_sections.size()) {
        return "ABS";
    }
    if (get_string(tab_start + elf_sections[shndx] -> sh_name, buffer).empty()) {
        return "UNDEF";
    }
    return std::to_string(shndx);
}

//get instruction opcode
unsigned int get_opcode(Elf32_Word instruction) {
    return (instruction & 0x7f);
}

//get instruction func3
unsigned int get_func3(Elf32_Word instruction) {
    return (instruction & 0x3800) >> 12;
}

//get instruction func7
unsigned int get_funct7(Elf32_Word instruction) {
    return (instruction & 0x7F000000) >> 25;
}

//get instruction part
int get_part(Elf32_Word instruction, int mask, int offset) {
    return (static_cast<int>(instruction) & mask) >> offset;
}

// Add extra labels to map labels
void add_labels(Elf32_Addr addr, Elf32_Word instruction) {
    if (get_opcode(instruction) == 0b1101111) { // JAL
        Elf32_Addr tmp_addr = addr +
                              (get_part(instruction, 0x7FE00000, 21) << 1) +
                              (get_part(instruction, 0x100000, 20) << 11) +
                              (get_part(instruction, 0xFF000, 12) << 12) +
                              (get_part(instruction, 0x80000000, 31) << 20);
        if (labels.find(tmp_addr) == labels.end()) {
            labels.insert({tmp_addr, "L" + std::to_string(cnt)});
            cnt++;
        }
    }

    else if (get_opcode(instruction) == 0b1100011 && get_func3(instruction) == 0b000) { // BEQ
        Elf32_Addr tmp_addr = addr +
                              (get_part(instruction, 0xF00, 8) << 1) +
                              (get_part(instruction, 0x7E000000, 25) << 5) +
                              (get_part(instruction, 0x80, 7) << 11) +
                              (get_part(instruction, 0x80000000, 31) << 12);
        if (labels.find(tmp_addr) == labels.end()) {
            labels.insert({tmp_addr, "L" + std::to_string(cnt)});
            cnt++;
        }
    } else if (get_opcode(instruction) == 0b1100011 && get_func3(instruction) == 0b001) { // BNE
        Elf32_Addr tmp_addr = addr +
                              (get_part(instruction, 0xF00, 8) << 1) +
                              (get_part(instruction, 0x7E000000, 25) << 5) +
                              (get_part(instruction, 0x80, 7) << 11) +
                              (get_part(instruction, 0x80000000, 31) << 12);
        if (labels.find(tmp_addr) == labels.end()) {
            labels.insert({tmp_addr, "L" + std::to_string(cnt)});
            cnt++;
        }
    } else if (get_opcode(instruction) == 0b1100011 && get_func3(instruction) == 0b100) { // BLT
        Elf32_Addr tmp_addr = addr +
                              (get_part(instruction, 0xF00, 8) << 1) +
                              (get_part(instruction, 0x7E000000, 25) << 5) +
                              (get_part(instruction, 0x80, 7) << 11) +
                              (get_part(instruction, 0x80000000, 31) << 12);
        if (labels.find(tmp_addr) == labels.end()) {
            labels.insert({tmp_addr, "L" + std::to_string(cnt)});
            cnt++;
        }
    } else if (get_opcode(instruction) == 0b1100011 && get_func3(instruction) == 0b101) {
        Elf32_Addr tmp_addr = addr +
                              (get_part(instruction, 0xF00, 8) << 1) +
                              (get_part(instruction, 0x7E000000, 25) << 5) +
                              (get_part(instruction, 0x80, 7) << 11) +
                              (get_part(instruction, 0x80000000, 31) << 12);
        if (labels.find(tmp_addr) == labels.end()) {
            labels.insert({tmp_addr, "L" + std::to_string(cnt)});
            cnt++;
        }
    } else if (get_opcode(instruction) == 0b1100011 && get_func3(instruction) == 0b110) { // BLTU
        Elf32_Addr tmp_addr = addr +
                              (get_part(instruction, 0xF00, 8) << 1) +
                              (get_part(instruction, 0x7E000000, 25) << 5) +
                              (get_part(instruction, 0x80, 7) << 11) +
                              (get_part(instruction, 0x80000000, 31) << 12);
        if (labels.find(tmp_addr) == labels.end()) {
            labels.insert({tmp_addr, "L" + std::to_string(cnt)});
            cnt++;
        }
    } else if (get_opcode(instruction) == 0b1100011 && get_func3(instruction) == 0b111) { // BGEU
        Elf32_Addr tmp_addr = addr +
                              (get_part(instruction, 0xF00, 8) << 1) +
                              (get_part(instruction, 0x7E000000, 25) << 5) +
                              (get_part(instruction, 0x80, 7) << 11) +
                              (get_part(instruction, 0x80000000, 31) << 12);
        if (labels.find(tmp_addr) == labels.end()) {
            labels.insert({tmp_addr, "L" + std::to_string(cnt)});
            cnt++;
        }
    }
}

//parse command
void parse_command(Elf32_Addr addr, Elf32_Word instruction) {
    if (get_opcode(instruction) == 0b0110111) { // LUI
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, 0x%x\n",
               addr,
               instruction,
               "lui",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               get_part(instruction, 0x7FFFF800, 12)
        );
    }

    else if (get_opcode(instruction) == 0b0010111) { // AUIPC
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, 0x%x\n",
               addr,
               instruction,
               "auipc",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               get_part(instruction, 0x7FFFF800, 12)
        );
    }

    else if (get_opcode(instruction) == 0b1100111 && get_func3(instruction) == 0b000) { // JALR
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %i(%s)\n",
               addr,
               instruction,
               "jalr",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               get_part(instruction, 0xFFF00000, 20),
               reg_names[get_part(instruction, rs1, 15)].c_str()
        );
    }

    else if (get_opcode(instruction) == 0b1101111) { // JAL
        Elf32_Addr label_addr = addr +
                                (get_part(instruction, 0x7FE00000, 21) << 1) +
                                (get_part(instruction, 0x100000, 20) << 11) +
                                (get_part(instruction, 0xFF000, 12) << 12) +
                                (get_part(instruction, 0x80000000, 31) << 20);
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, 0x%x <%s>\n",
               addr,
               instruction,
               "jal",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               label_addr,
               labels[label_addr].c_str()
        );
    }

    else if (get_opcode(instruction) == 0b1100011 && get_func3(instruction) == 0b000) { // BEQ
        Elf32_Addr label_addr = addr +
                                (get_part(instruction, 0xF00, 8) << 1) +
                                (get_part(instruction, 0x7E000000, 25) << 5) +
                                (get_part(instruction, 0x80, 7) << 11) +
                                (get_part(instruction, 0x80000000, 31) << 12);
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, 0x%x <%s>\n",
               addr,
               instruction,
               "beq",
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str(),
               label_addr,
               labels[label_addr].c_str()
        );
    } else if (get_opcode(instruction) == 0b1100011 && get_func3(instruction) == 0b001) { // BNE
        Elf32_Addr label_addr = addr +
                                (get_part(instruction, 0xF00, 8) << 1) +
                                (get_part(instruction, 0x7E000000, 25) << 5) +
                                (get_part(instruction, 0x80, 7) << 11) +
                                (get_part(instruction, 0x80000000, 31) << 12);
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, 0x%x <%s>\n",
               addr,
               instruction,
               "bne",
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str(),
               label_addr,
               labels[label_addr].c_str()
        );
    } else if (get_opcode(instruction) == 0b1100011 && get_func3(instruction) == 0b100) { // BLT
        Elf32_Addr label_addr = addr +
                                (get_part(instruction, 0xF00, 8) << 1) +
                                (get_part(instruction, 0x7E000000, 25) << 5) +
                                (get_part(instruction, 0x80, 7) << 11) +
                                (get_part(instruction, 0x80000000, 31) << 12);
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, 0x%x <%s>\n",
               addr,
               instruction,
               "blt",
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str(),
               label_addr,
               labels[label_addr].c_str()
        );
    } else if (get_opcode(instruction) == 0b1100011 && get_func3(instruction) == 0b101) { // BGE
        Elf32_Addr label_addr = addr +
                                (get_part(instruction, 0xF00, 8) << 1) +
                                (get_part(instruction, 0x7E000000, 25) << 5) +
                                (get_part(instruction, 0x80, 7) << 11) +
                                (get_part(instruction, 0x80000000, 31) << 12);
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, 0x%x <%s>\n",
               addr,
               instruction,
               "bge",
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str(),
               label_addr,
               labels[label_addr].c_str()
        );
    } else if (get_opcode(instruction) == 0b1100011 && get_func3(instruction) == 0b110) { // BLTU
        Elf32_Addr label_addr = addr +
                                (get_part(instruction, 0xF00, 8) << 1) +
                                (get_part(instruction, 0x7E000000, 25) << 5) +
                                (get_part(instruction, 0x80, 7) << 11) +
                                (get_part(instruction, 0x80000000, 31) << 12);
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, 0x%x <%s>\n",
               addr,
               instruction,
               "bltu",
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str(),
               label_addr,
               labels[label_addr].c_str()
        );
    } else if (get_opcode(instruction) == 0b1100011 && get_func3(instruction) == 0b111) { // BGEU
        Elf32_Addr label_addr = addr +
                                (get_part(instruction, 0xF00, 8) << 1) +
                                (get_part(instruction, 0x7E000000, 25) << 5) +
                                (get_part(instruction, 0x80, 7) << 11) +
                                (get_part(instruction, 0x80000000, 31) << 12);
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, 0x%x <%s>\n",
               addr,
               instruction,
               "bgeu",
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str(),
               label_addr,
               labels[label_addr].c_str()
        );
    }

    else if (get_opcode(instruction) == 0b0000011 && get_func3(instruction) == 0b000) { // LB
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %i(%s)\n",
               addr,
               instruction,
               "lb",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               get_part(instruction, 0xFFF00000, 20),
               reg_names[get_part(instruction, rs1, 15)].c_str()
        );
    } else if (get_opcode(instruction) == 0b0000011 && get_func3(instruction) == 0b001) { // LH
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %i(%s)\n",
               addr,
               instruction,
               "lh",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               get_part(instruction, 0xFFF00000, 20),
               reg_names[get_part(instruction, rs1, 15)].c_str()
        );
    } else if (get_opcode(instruction) == 0b0000011 && get_func3(instruction) == 0b010) { // LW
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %i(%s)\n",
               addr,
               instruction,
               "lw",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               get_part(instruction, 0xFFF00000, 20),
               reg_names[get_part(instruction, rs1, 15)].c_str()
        );
    } else if (get_opcode(instruction) == 0b0000011 && get_func3(instruction) == 0b100) { // LBU
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %i(%s)\n",
               addr,
               instruction,
               "lbu",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               get_part(instruction, 0xFFF00000, 20),
               reg_names[get_part(instruction, rs1, 15)].c_str()
        );
    } else if (get_opcode(instruction) == 0b0000011 && get_func3(instruction) == 0b101) { // LHU
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %i(%s)\n",
               addr,
               instruction,
               "lhu",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               get_part(instruction, 0xFFF00000, 20),
               reg_names[get_part(instruction, rs1, 15)].c_str()
        );
    }

    else if (get_opcode(instruction) == 0b0100011 && get_func3(instruction) == 0b000) { // SB
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %i(%s)\n",
                addr,
                instruction,
                "sb",
                reg_names[get_part(instruction, rs2, 20)].c_str(),
                (get_part(instruction, 0xFE000000, 25) << 5) + get_part(instruction, 0xF80, 7),
                reg_names[get_part(instruction, rs1, 15)].c_str()
        );
    } else if (get_opcode(instruction) == 0b0100011 && get_func3(instruction) == 0b001) { // SH
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %i(%s)\n",
                addr,
                instruction,
                "sh",
                reg_names[get_part(instruction, rs2, 20)].c_str(),
                (get_part(instruction, 0xFE000000, 25) << 5) + get_part(instruction, 0xF80, 7),
                reg_names[get_part(instruction, rs1, 15)].c_str()
        );
    } else if (get_opcode(instruction) == 0b0100011 && get_func3(instruction) == 0b010) { // SW
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %i(%s)\n",
               addr,
               instruction,
               "sw",
                reg_names[get_part(instruction, rs2, 20)].c_str(),
               (get_part(instruction, 0xFE000000, 25) << 5) + get_part(instruction, 0xF80, 7),
                reg_names[get_part(instruction, rs1, 15)].c_str()
        );
    }

    else if ( // SLLI
            get_opcode(instruction) == 0b0010011 &&
            get_func3(instruction) == 0b001 &&
            get_funct7(instruction) == 0b0000000
    ) {
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %i\n",
               addr,
               instruction,
               "slli",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               get_part(instruction, rs2, 20)
        );
    } else if ( // SRLI
            get_opcode(instruction) == 0b0010011 &&
            get_func3(instruction) == 0b101 &&
            get_funct7(instruction) == 0b0000000
    ) {
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %i\n",
               addr,
               instruction,
               "srli",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               get_part(instruction, rs2, 20)
        );
    } else if ( // SRAI
            get_opcode(instruction) == 0b0010011 &&
            get_func3(instruction) == 0b101 &&
            get_funct7(instruction) == 0b0100000
    ) {
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %i\n",
               addr,
               instruction,
               "slli",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               get_part(instruction, rs2, 20)
        );
    } else if ( // ADD
            get_opcode(instruction) == 0b0110011 &&
            get_func3(instruction) == 0b000 &&
            get_funct7(instruction) == 0b0000000
    ) {
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %s\n",
               addr,
               instruction,
               "add",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str()
        );
    } else if ( // SUB
            get_opcode(instruction) == 0b0110011 &&
            get_func3(instruction) == 0b000 &&
            get_funct7(instruction) == 0b0100000
    ) {
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %s\n",
               addr,
               instruction,
               "sub",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str()
        );
    } else if ( // SLL
            get_opcode(instruction) == 0b0110011 &&
            get_func3(instruction) == 0b001 &&
            get_funct7(instruction) == 0b0000000
    ) {
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %s\n",
               addr,
               instruction,
               "sll",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str()
        );
    } else if ( // SLT
            get_opcode(instruction) == 0b0110011 &&
            get_func3(instruction) == 0b010 &&
            get_funct7(instruction) == 0b0000000
    ) {
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %s\n",
               addr,
               instruction,
               "slt",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str()
        );
    } else if ( // SLTU
            get_opcode(instruction) == 0b0110011 &&
            get_func3(instruction) == 0b011 &&
            get_funct7(instruction) == 0b0000000
    ) {
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %s\n",
               addr,
               instruction,
               "sltu",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str()
        );
    } else if ( // XOR
            get_opcode(instruction) == 0b0110011 &&
            get_func3(instruction) == 0b100 &&
            get_funct7(instruction) == 0b0000000
    ) {
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %s\n",
               addr,
               instruction,
               "xor",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str()
        );
    } else if ( // SRL
            get_opcode(instruction) == 0b0110011 &&
            get_func3(instruction) == 0b101 &&
            get_funct7(instruction) == 0b0000000
    ) {
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %s\n",
               addr,
               instruction,
               "srl",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str()
        );
    } else if ( // SRA
            get_opcode(instruction) == 0b0110011 &&
            get_func3(instruction) == 0b101 &&
            get_funct7(instruction) == 0b0100000
    ) {
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %s\n",
               addr,
               instruction,
               "sra",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str()
        );
    } else if ( // OR
            get_opcode(instruction) == 0b0110011 &&
            get_func3(instruction) == 0b110 &&
            get_funct7(instruction) == 0b0000000
    ) {
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %s\n",
               addr,
               instruction,
               "or",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str()
        );
    } else if ( // AND
            get_opcode(instruction) == 0b0110011 &&
            get_func3(instruction) == 0b111 &&
            get_funct7(instruction) == 0b0000000
    ) {
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %s\n",
               addr,
               instruction,
               "and",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str()
        );
    } else if ( // MUL
            get_opcode(instruction) == 0b0110011 &&
            get_func3(instruction) == 0b000 &&
            get_funct7(instruction) == 0b0000001
    ) {
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %s\n",
               addr,
               instruction,
               "mul",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str()
        );
    } else if ( // MULH
            get_opcode(instruction) == 0b0110011 &&
            get_func3(instruction) == 0b001 &&
            get_funct7(instruction) == 0b0000001
    ) {
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %s\n",
               addr,
               instruction,
               "mulh",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str()
        );
    } else if ( // MULHSU
            get_opcode(instruction) == 0b0110011 &&
            get_func3(instruction) == 0b010 &&
            get_funct7(instruction) == 0b0000001
    ) {
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %s\n",
               addr,
               instruction,
               "mulhsu",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str()
        );
    } else if ( // MULHU
            get_opcode(instruction) == 0b0110011 &&
            get_func3(instruction) == 0b011 &&
            get_funct7(instruction) == 0b0000001
    ) {
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %s\n",
               addr,
               instruction,
               "mulhu",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str()
        );
    } else if ( // DIV
            get_opcode(instruction) == 0b0110011 &&
            get_func3(instruction) == 0b100 &&
            get_funct7(instruction) == 0b0000001
    ) {
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %s\n",
               addr,
               instruction,
               "div",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str()
        );
    } else if ( // DIVU
            get_opcode(instruction) == 0b0110011 &&
            get_func3(instruction) == 0b101 &&
            get_funct7(instruction) == 0b0000001
    ) {
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %s\n",
               addr,
               instruction,
               "divu",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str()
        );
    } else if ( // REM
            get_opcode(instruction) == 0b0110011 &&
            get_func3(instruction) == 0b110 &&
            get_funct7(instruction) == 0b0000001
    ) {
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %s\n",
               addr,
               instruction,
               "rem",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str()
        );
    } else if ( // REMU
            get_opcode(instruction) == 0b0110011 &&
            get_func3(instruction) == 0b111 &&
            get_funct7(instruction) == 0b0000001
    ) {
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %s\n",
               addr,
               instruction,
               "remu",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               reg_names[get_part(instruction, rs2, 20)].c_str()
        );
    }

    else if (get_opcode(instruction) == 0b0010011 && get_func3(instruction) == 0b000) { // ADDI
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %i\n",
               addr,
               instruction,
               "addi",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               get_part(instruction, 0xFFF00000, 20)
        );
    } else if (get_opcode(instruction) == 0b0010011 && get_func3(instruction) == 0b010) { // SLTI
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %i\n",
               addr,
               instruction,
               "slti",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               get_part(instruction, 0xFFF00000, 20)
        );
    } else if (get_opcode(instruction) == 0b0010011 && get_func3(instruction) == 0b011) { // SLTIU
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %i\n",
               addr,
               instruction,
               "sltiu",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               get_part(instruction, 0xFFF00000, 20)
        );
    } else if (get_opcode(instruction) == 0b0010011 && get_func3(instruction) == 0b100) { // XORI
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %i\n",
               addr,
               instruction,
               "xori",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               get_part(instruction, 0xFFF00000, 20)
        );
    } else if (get_opcode(instruction) == 0b0010011 && get_func3(instruction) == 0b110) { // ORI
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %i\n",
               addr,
               instruction,
               "ori",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               get_part(instruction, 0xFFF00000, 20)
        );
    } else if (get_opcode(instruction) == 0b0010011 && get_func3(instruction) == 0b111) { // ANDI
        fprintf(out,"   %05x:\t%08x\t%7s\t%s, %s, %i\n",
               addr,
               instruction,
               "andi",
               reg_names[get_part(instruction, rd, 7)].c_str(),
               reg_names[get_part(instruction, rs1, 15)].c_str(),
               get_part(instruction, 0xFFF00000, 20)
        );
    }

    else if (instruction == 0b00000000000000000000000001110011) { // ECALL
        fprintf(out,"   %05x:\t%08x\t%7s\n", addr, instruction, "ecall");
    } else if (instruction == 0b00000000000100000000000001110011) { // EBREAK
        fprintf(out,"   %05x:\t%08x\t%7s\n", addr, instruction, "ebreak");
    } else { // unknown_operation
        fprintf(out,"   %05x:\t%08x\t%7s\n", addr, instruction, "unknown_instruction");
    }

}

int main(int argc, char *argv[]) {
    std::ifstream in;
    std::filesystem::path input_file_name, output_file_name;

    if (argc == 3) {
        input_file_name = argv[1];
        output_file_name = argv[2];
    } else {
        std::cerr << "Expected 2 arguments, found " << argc - 1 << "." << std::endl;
        return 0;
    }

    try {
        in = std::ifstream(input_file_name, std::ios::in | std::ios::binary); // construct byte stream
        in.exceptions(std::ifstream::failbit);

        uintmax_t size_of_file = std::filesystem::file_size(input_file_name);
        char* buffer = new char[size_of_file];
        in.read(buffer, static_cast<long>(size_of_file)); // read file


        auto elf_header = reinterpret_cast<Elf32_Ehdr*>(buffer); // parse elf header

        // check that we have elf
        if (
                elf_header -> e_ident[EI_MAG0] != 0x7f ||
                elf_header -> e_ident[EI_MAG1] != 'E' ||
                elf_header -> e_ident[EI_MAG2] != 'L' ||
                elf_header -> e_ident[EI_MAG3] != 'F'
        ) {
            std::cout << "Provided for input file is not a elf file." << std::endl;
            return 0;
        }

        if (elf_header -> e_ident[EI_CLASS] == ELFCLASS32) {
            std::cout << "Provided file is not a elf 32 bit file." << std::endl;
            return 0;
        }

        // check that we have RISC-V
        if (elf_header -> e_machine != EM_RISCV) {
            std::cout << "Provided file does not support RISC-V." << std::endl;
            return 0;
        }

        // parse section header table
        std::vector<Elf32_Shdr*> elf_section_header_table(elf_header -> e_shnum);
        for (size_t i = 0; i < elf_header -> e_shnum; ++i) {
            elf_section_header_table[i] =
                    reinterpret_cast<Elf32_Shdr*>(buffer + elf_header -> e_shoff + elf_header -> e_shentsize * i);
        }

        // find .symtab and .text section
        Elf32_Shdr* symtab = nullptr;
        Elf32_Shdr* text = nullptr;
        Elf32_Shdr* strtab = nullptr;
        unsigned int string_table_entry = elf_section_header_table[elf_header -> e_shstrndx] -> sh_offset;
        for (Elf32_Shdr* &i : elf_section_header_table) {
            if (get_string(string_table_entry + i -> sh_name, buffer) == ".symtab") {
                symtab = i;
            }
            if (get_string(string_table_entry + i -> sh_name, buffer) == ".text") {
                text = i;
            }
            if (get_string(string_table_entry + i -> sh_name, buffer) == ".strtab") {
                strtab = i;
            }
        }

        //parse .symtab
        std::vector <Elf32_Sym*> elf_symtab(symtab -> sh_size / sizeof(Elf32_Sym));
        for (size_t i = 0; i < elf_symtab.size(); ++i) {
            elf_symtab[i] = reinterpret_cast<Elf32_Sym*>(buffer + symtab -> sh_offset + sizeof(Elf32_Sym) * i);
        }
        for (Elf32_Sym* &i : elf_symtab) {
            if (!get_string(strtab->sh_offset + i->st_name, buffer).empty()) {
                labels.insert({i->st_value, get_string(strtab->sh_offset + i->st_name, buffer)});
            }
        }

        // parse .text
        if (text -> sh_size % 4 != 0) {
            std::cout << "Expected file that contain 4 byte RISC-V commands." << std::endl;
            return 0;
        }

        out = fopen(argv[2], "w");
        fprintf(out,".text\n");

        for (size_t i = 0; i < text -> sh_size; i += 4) {
            add_labels(text -> sh_addr + i,
                       *reinterpret_cast<Elf32_Word*>(buffer + text -> sh_offset + i)
            );
        }

        for (size_t i = 0; i < text -> sh_size; i += 4) {
            if (labels.find(text -> sh_addr + i) != labels.end()) {
                fprintf(out,"%08x   <%s>:\n",
                       text -> sh_addr + i,
                       labels[text -> sh_addr + i].c_str()
                );
            }
            parse_command(text -> sh_addr + i, *reinterpret_cast<Elf32_Word*>(buffer + text -> sh_offset + i));
        }



        // printing .symtab
        fprintf(out,"\n.symtab\n");
        fprintf(out,"Symbol Value              Size Type 	Bind 	 Vis   	   Index Name\n");
        int num = 0;
        for (Elf32_Sym* &i : elf_symtab) {
            fprintf(out,
                    "[%4i] 0x%-15X %5i %-8s %-8s %-8s %6s %s\n",
                    num,
                    i -> st_value,
                    i -> st_size,
                    get_symtab_elem_type(i -> st_info).c_str(),
                    get_symtab_elem_bind(i -> st_info).c_str(),
                    get_symtab_elem_vis(i -> st_other).c_str(),
                    get_symtab_elem_indx(
                            i -> st_shndx,
                            elf_section_header_table,
                            buffer,
                            string_table_entry
                            ).c_str(),
                    get_string(strtab -> sh_offset + i -> st_name, buffer).c_str()
                    );
            ++num;
        }
        delete[]buffer;
    } catch (const std::ios_base::failure& e) {
        std::cout << e.what() << std::endl;
    }
    return 0;
}
