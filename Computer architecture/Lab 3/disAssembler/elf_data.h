//
// Created by nrtv on 14/12/22.
//

#ifndef DISASSEMBLER_ELF_DATA_H
#define DISASSEMBLER_ELF_DATA_H

#include <cstdint>
#include <fstream>
#include <algorithm>

typedef uint32_t Elf32_Addr;
typedef uint16_t Elf32_Half;
typedef uint32_t Elf32_Off;
typedef	int32_t  Elf32_Sword;
typedef uint32_t Elf32_Word;

constexpr int EI_NIDENT = 16;
constexpr int ELF_HEADER_SIZE = 52;
constexpr int EI_MAG0 = 0;
constexpr int EI_MAG1 = 1;
constexpr int EI_MAG2 = 2;
constexpr int EI_MAG3 = 3;
constexpr int EI_CLASS = 4;
constexpr unsigned char EM_RISCV = 243;
constexpr unsigned char ELFCLASS32 = 2;

struct Elf32_Ehdr {
    unsigned char e_ident[EI_NIDENT];
    Elf32_Half	e_type;
    Elf32_Half	e_machine;
    Elf32_Word	e_version;
    Elf32_Addr	e_entry;
    Elf32_Off	e_phoff;
    Elf32_Off	e_shoff;
    Elf32_Word	e_flags;
    Elf32_Half	e_ehsize;
    Elf32_Half	e_phentsize;
    Elf32_Half	e_phnum;
    Elf32_Half	e_shentsize;
    Elf32_Half	e_shnum;
    Elf32_Half	e_shstrndx;
};

struct Elf32_Shdr {
    Elf32_Word	sh_name;
    Elf32_Word	sh_type;
    Elf32_Word	sh_flags;
    Elf32_Addr	sh_addr;
    Elf32_Off	sh_offset;
    Elf32_Word	sh_size;
    Elf32_Word	sh_link;
    Elf32_Word	sh_info;
    Elf32_Word	sh_addralign;
    Elf32_Word	sh_entsize;
};

struct Elf32_Sym{
    Elf32_Word	st_name;
    Elf32_Addr	st_value;
    Elf32_Word	st_size;
    unsigned char st_info;
    unsigned char st_other;
    Elf32_Half	st_shndx;
};

#endif //DISASSEMBLER_ELF_DATA_H
