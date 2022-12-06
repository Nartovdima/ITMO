`include "mem.sv"

module cache(
    input wire CLK, 
    input wire C_DUMP, 
    input wire RESET, 

    input wire [14: 0] a1_bus, 
    inout wire [15: 0] d1_bus,
    inout wire [2: 0] c1_bus,

    input wire [14: 0] a2_bus,
    inout wire [15: 0] d2_bus,
    inout wire [1: 0] c2_bus,

    output wire [31 : 0] cache_miss,
    output wire [31 : 0] cache_hit
);

`define CACHE_WAY 2

`define ADDR1_BUS_SIZE 15
`define DATA1_BUS_SIZE 16
`define CTR1_BUS_SIZE 3

`define ADDR2_BUS_SIZE 15
`define DATA2_BUS_SIZE 16
`define CTR2_BUS_SIZE 2

`define CACHE_LINE_SIZE 128
`define CACHE_FULL_LINE_SIZE 141
`define CACHE_SETS_COUNT 32
`define CACHE_LINE_COUNT 64
`define CACHE_ADDR_SIZE 19
`define CACHE_OFFSET_SIZE 4
`define CACHE_VALID 1
`define CACHE_DIRTY 1
`define CACHE_AGE 1
`define CACHE_TAG_SIZE 10
`define CACHE_LINE_SIZE 128
`define CACHE_SETS_SIZE 5
`define CACHE_WAY 2

`define C1_READ8 3'b001
`define C1_READ16 3'b010
`define C1_READ32 3'b011
`define C1_WRITE8 3'b101
`define C1_WRITE16 3'b110
`define C1_WRITE32 3'b111
`define C1_NOP 3'b000
`define C1_INVALIDATE_LINE 3'b100
`define C1_RESPONSE 3'b111

`define C2_NOP 2'b00
`define C2_RESPONSE 2'b01
`define C2_READ_LINE 2'b10
`define C2_WRITE_LINE 2'b11

`define CACHE_MISS 4
`define CACHE_HIT 6


reg [`ADDR1_BUS_SIZE - 1: 0] b1_addr;
reg [`DATA1_BUS_SIZE - 1: 0] b1_data;
reg [`CTR1_BUS_SIZE - 1: 0] b1_command;

reg [`ADDR2_BUS_SIZE - 1: 0] b2_addr;
reg [`DATA2_BUS_SIZE - 1: 0] b2_data;
reg [`CTR2_BUS_SIZE - 1: 0] b2_command;

reg [32 : 0] miss;
reg [32 : 0] hit;

integer i = 0;
integer j = 0;
integer cnt = 0;
integer cache_hit = 0;
integer shift = 0;
integer vnt = 0;
integer num_of_line = 0;


assign a1_bus = b1_addr;
assign d1_bus = b1_data;
assign c1_bus = b1_command;

assign a2_bus = b2_addr;
assign d2_bus = b2_data;
assign c2_bus = b2_command;

assign cache_hit = hit;
assign cache_miss = miss;

reg [`CACHE_FULL_LINE_SIZE - 1 : 0] cache[0 : `CACHE_SETS_COUNT - 1][0 : `CACHE_WAY - 1]; 

reg [`DATA1_BUS_SIZE - 1 : 0] tmp_data1;
reg [`DATA2_BUS_SIZE - 1 : 0] tmp_data2;
reg [`CACHE_ADDR_SIZE - 1 : 0] tmp_addr;
reg [`CACHE_TAG_SIZE + `CACHE_SETS_SIZE - 1 : 0] tmp_addr2;
reg [`CACHE_LINE_SIZE - 1 : 0] tmp_buffer;
reg [`CACHE_SETS_SIZE : 0] tmp_set;

initial begin
    b1_addr [`ADDR1_BUS_SIZE - 1: 0] = 15'bz;
    b1_data [`DATA1_BUS_SIZE - 1: 0] = 16'bz;
    b1_command [`CTR1_BUS_SIZE - 1: 0] = 3'bz;

    b2_addr [`ADDR2_BUS_SIZE - 1 : 0] = 15'bz;
    b2_data [`DATA2_BUS_SIZE - 1 : 0] = 16'bz;
    b2_command [`CTR2_BUS_SIZE - 1 : 0] = `C2_NOP;

    for (i = 0; i < `CACHE_LINE_COUNT; i += 1) begin
        cache[i][`CACHE_FULL_LINE_SIZE - 1] = 0; 
    end
end

mem _mem(
    CLK, 
    M_DUMP,
    RESET,
    a2_bus,
    d2_bus,
    c2_bus
);

always @(C_DUMP) begin
    $dumpfile("output.txt");
end

always @(RESET) begin
    b1_addr [`ADDR2_BUS_SIZE - 1: 0] = 15'bz;
    b1_data [`DATA2_BUS_SIZE - 1: 0] = 16'bz;
    b1_command [`CTR2_BUS_SIZE - 1: 0] = 3'bz;

    b2_addr [`ADDR2_BUS_SIZE - 1 : 0] = 15'bz;
    b2_data [`DATA2_BUS_SIZE - 1 : 0] = 16'bz;
    b2_command [`CTR2_BUS_SIZE - 1 : 0] = `C2_NOP;

    for (i = 0; i < `CACHE_LINE_COUNT; i += 1) begin
        cache[i][`CACHE_FULL_LINE_SIZE - 1] = 0; 
    end
end

always @(posedge CLK) begin
    case(b1_command) 
        (`C1_READ8) : begin 
            if (cnt == 0) begin
                tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE] = a1_bus;
                cnt += 1;
            end if (cnt == 1) begin
                tmp_addr[`CACHE_OFFSET_SIZE - 1 : 0] = a1_bus; // mb bug
                cnt += 1;
            end else begin
                // find cacheLine
                if (cnt == 2) begin
                    tmp_set = tmp_addr[`CACHE_ADDR_SIZE - 1 - `CACHE_TAG_SIZE : `CACHE_OFFSET_SIZE];
                    if (
                        cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 4 : `CACHE_LINE_SIZE] == 
                        tmp_addr[`CACHE_ADDR_SIZE - `CACHE_TAG_SIZE - 1: `CACHE_OFFSET_SIZE]
                    ) begin
                        cache_hit = 1;
                    end

                    if (
                        cache[tmp_set][1][`CACHE_FULL_LINE_SIZE - 4 : `CACHE_LINE_SIZE] == 
                        tmp_addr[`CACHE_ADDR_SIZE - `CACHE_TAG_SIZE - 1: `CACHE_OFFSET_SIZE]
                    ) begin
                        cache_hit = 2;
                    end
                    cnt += 1;
                end
                // cache_hit
                if (cache_hit != 0) begin
                    hit++;
                    // cache_hit += 1;
                    #(`CACHE_HIT - 1);
                    b1_command = `C1_RESPONSE;
                    // send answer
                    vnt = 7;
                    for (i = 0; i < 8; i++) begin
                        tmp_data1[vnt] = cache[tmp_set][cache_hit - 1][tmp_addr[`CACHE_OFFSET_SIZE - 1 : 0] * 8 + i];
                    end
                    b1_data = tmp_data1;
                    #1;
                    b1_command = 3'bz;
                end else begin
                    #(`CACHE_MISS);
                    miss++;
                    // go to mem
                    if (cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 1] == 1'b0) begin
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][0][shift + i] = tmp_data2[j];   
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                    end else if (cache[tmp_set][1][`CACHE_FULL_LINE_SIZE - 1] == 1'b0) begin
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][1][shift + i] = tmp_data2[j];    
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                        num_of_line = 1;
                    end else if (cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 3] == 1'b1) begin
                        // write back if needed
                        if (cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 2] == 1'b1) begin
                            #1;
                            b2_command = `C2_WRITE_LINE;
                            tmp_addr2[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE] = cache[tmp_set][0][`CACHE_LINE_SIZE + `CACHE_TAG_SIZE - 1 : `CACHE_LINE_SIZE];
                            tmp_addr2[`CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE - 1 : `CACHE_OFFSET_SIZE] = tmp_set;
                            b2_addr = tmp_addr2;

                            if (cnt < 3 + 8) begin
                                vnt = 15;
                                for (i = 0; i < 16; i += 1) begin
                                    tmp_data2[vnt] = cache[tmp_set][0][i + shift];
                                    vnt -= 1;
                                    #2;
                                end
                                shift += 16;
                            end
                            b2_command = 2'bz;
                            #1;
                            wait(b2_command == `C2_RESPONSE);
                        end
                        shift = 0;
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][0][shift + i] = tmp_data2[j];    
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                        num_of_line = 0;
                    end else begin
                        // write back if needed
                        // write back if needed
                        if (cache[tmp_set][1][`CACHE_FULL_LINE_SIZE - 2] == 1'b1) begin
                            #1;
                            b2_command = `C2_WRITE_LINE;
                            tmp_addr2[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE] = cache[tmp_set][0][`CACHE_LINE_SIZE + `CACHE_TAG_SIZE - 1 : `CACHE_LINE_SIZE];
                            tmp_addr2[`CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE - 1 : `CACHE_OFFSET_SIZE] = tmp_set;
                            b2_addr = tmp_addr2;

                            if (cnt < 3 + 8) begin
                                vnt = 15;
                                for (i = 0; i < 16; i += 1) begin
                                    tmp_data2[vnt] = cache[tmp_set][1][i + shift];
                                    vnt -= 1;
                                    #2;
                                end
                                shift += 16;
                            end
                            b2_command = 2'bz;
                            #1;
                            wait(b2_command == `C2_RESPONSE);
                        end
                        shift = 0;
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][1][shift + i] = tmp_data2[j];    
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                        num_of_line = 1;
                    end
                end
                #1;
                b1_command = `C1_RESPONSE;
                vnt = 7;
                for (i = 0; i < 8; i++) begin
                    tmp_data1[vnt] = cache[tmp_set][num_of_line][tmp_addr[`CACHE_OFFSET_SIZE - 1 : 0] * 8 + i];
                end
                b1_data = tmp_data1;
                #1;
                b1_command = 3'bz;
                // send cpu 
            end 
            cnt = 0;
        end

        (`C1_READ16) : begin //must-have
            // find cache line
            // if yes print
            // if no go to mem
            // if have empty space cache
            // else delete old
            // if old modified write back
            // set new one line
            if (cnt == 0) begin
                tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE] = a1_bus;
                cnt += 1;
            end if (cnt == 1) begin
                tmp_addr[`CACHE_OFFSET_SIZE - 1 : 0] = a1_bus; // mb bug
                cnt += 1;
            end else begin
                // find cacheLine
                if (cnt == 2) begin
                    tmp_set = tmp_addr[`CACHE_ADDR_SIZE - 1 - `CACHE_TAG_SIZE : `CACHE_OFFSET_SIZE];
                    if (
                        cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 4 : `CACHE_LINE_SIZE] == 
                        tmp_addr[`CACHE_ADDR_SIZE - `CACHE_TAG_SIZE - 1: `CACHE_OFFSET_SIZE]
                    ) begin
                        cache_hit = 1;
                    end

                    if (
                        cache[tmp_set][1][`CACHE_FULL_LINE_SIZE - 4 : `CACHE_LINE_SIZE] == 
                        tmp_addr[`CACHE_ADDR_SIZE - `CACHE_TAG_SIZE - 1: `CACHE_OFFSET_SIZE]
                    ) begin
                        cache_hit = 2;
                    end
                    cnt += 1;
                end
                // cache_hit
                if (cache_hit != 0) begin
                    // cache_hit += 1;
                    #(`CACHE_HIT - 1);
                    hit++;
                    b1_command = `C1_RESPONSE;
                    // send answer
                    vnt = 15;
                    for (i = 0; i < 16; i++) begin
                        tmp_data1[vnt] = cache[tmp_set][cache_hit - 1][tmp_addr[`CACHE_OFFSET_SIZE - 1 : 0] * 8 + i];
                    end
                    b1_data = tmp_data1;
                    #1;
                    b1_command = 3'bz;
                end else begin
                    #(`CACHE_MISS);
                    miss++;
                    // go to mem
                    if (cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 1] == 1'b0) begin
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][0][shift + i] = tmp_data2[j];    
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                    end else if (cache[tmp_set][1][`CACHE_FULL_LINE_SIZE - 1] == 1'b0) begin
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][1][shift + i] = tmp_data2[j];  
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                        num_of_line = 1;
                    end else if (cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 3] == 1'b1) begin
                        // write back if needed
                        if (cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 2] == 1'b1) begin
                            #1;
                            b2_command = `C2_WRITE_LINE;
                            tmp_addr2[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE] = cache[tmp_set][0][`CACHE_LINE_SIZE + `CACHE_TAG_SIZE - 1 : `CACHE_LINE_SIZE];
                            tmp_addr2[`CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE - 1 : `CACHE_OFFSET_SIZE] = tmp_set;
                            b2_addr = tmp_addr2;

                            if (cnt < 3 + 8) begin
                                vnt = 15;
                                for (i = 0; i < 16; i += 1) begin
                                    tmp_data2[vnt] = cache[tmp_set][0][i + shift];
                                    vnt -= 1;
                                    #2;
                                end
                                shift += 16;
                            end
                            b2_command = 2'bz;
                            #1;
                            wait(b2_command == `C2_RESPONSE);
                        end
                        shift = 0;
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][0][shift + i] = tmp_data2[j];  
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                        num_of_line = 0;
                    end else begin
                        // write back if needed
                        // write back if needed
                        if (cache[tmp_set][1][`CACHE_FULL_LINE_SIZE - 2] == 1'b1) begin
                            #1;
                            b2_command = `C2_WRITE_LINE;
                            tmp_addr2[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE] = cache[tmp_set][0][`CACHE_LINE_SIZE + `CACHE_TAG_SIZE - 1 : `CACHE_LINE_SIZE];
                            tmp_addr2[`CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE - 1 : `CACHE_OFFSET_SIZE] = tmp_set;
                            b2_addr = tmp_addr2;

                            if (cnt < 3 + 8) begin
                                vnt = 15;
                                for (i = 0; i < 16; i += 1) begin
                                    tmp_data2[vnt] = cache[tmp_set][1][i + shift];
                                    vnt -= 1;
                                    #2;
                                end
                                shift += 16;
                            end
                            b2_command = 2'bz;
                            #1;
                            wait(b2_command == `C2_RESPONSE);
                        end
                        shift = 0;
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][1][shift + i] = tmp_data2[j];   
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                        num_of_line = 1;
                    end
                end
                #1;
                b1_command = `C1_RESPONSE;
                vnt = 15;
                for (i = 0; i < 16; i++) begin
                    tmp_data1[vnt] = cache[tmp_set][num_of_line][tmp_addr[`CACHE_OFFSET_SIZE - 1 : 0] * 8 + i];
                end
                b1_data = tmp_data1;
                #1;
                b1_command = 3'bz;
                // send cpu 
            end 
            cnt = 0;
        end

        (`C1_READ32) : begin
            if (cnt == 0) begin
                tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE] = a1_bus;
                cnt += 1;
            end if (cnt == 1) begin
                tmp_addr[`CACHE_OFFSET_SIZE - 1 : 0] = a1_bus; // mb bug
                cnt += 1;
            end else begin
                // find cacheLine
                if (cnt == 2) begin
                    tmp_set = tmp_addr[`CACHE_ADDR_SIZE - 1 - `CACHE_TAG_SIZE : `CACHE_OFFSET_SIZE];
                    if (
                        cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 4 : `CACHE_LINE_SIZE] == 
                        tmp_addr[`CACHE_ADDR_SIZE - `CACHE_TAG_SIZE - 1: `CACHE_OFFSET_SIZE]
                    ) begin
                        cache_hit = 1;
                    end

                    if (
                        cache[tmp_set][1][`CACHE_FULL_LINE_SIZE - 4 : `CACHE_LINE_SIZE] == 
                        tmp_addr[`CACHE_ADDR_SIZE - `CACHE_TAG_SIZE - 1: `CACHE_OFFSET_SIZE]
                    ) begin
                        cache_hit = 2;
                    end
                    cnt += 1;
                end
                // cache_hit
                if (cache_hit != 0) begin
                    // cache_hit += 1;
                    #(`CACHE_HIT - 1);
                    hit++;
                    b1_command = `C1_RESPONSE;
                    // send answer
                    vnt = 15;
                    for (i = 0; i < 16; i++) begin
                        tmp_data1[vnt] = cache[tmp_set][cache_hit - 1][tmp_addr[`CACHE_OFFSET_SIZE - 1 : 0] * 8 + i];
                    end
                    #2
                    b1_data = tmp_data1;
                    vnt = 15;
                    for (i = 0; i < 16; i++) begin
                        tmp_data1[vnt] = cache[tmp_set][cache_hit - 1][tmp_addr[`CACHE_OFFSET_SIZE - 1 : 0] * 8 + i];
                    end
                    b1_data = tmp_data1;
                    #1;
                    b1_command = 3'bz;
                end else begin
                    #(`CACHE_MISS);
                    miss++;
                    // go to mem
                    if (cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 1] == 1'b0) begin
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][0][shift + i] = tmp_data2[j];   
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                    end else if (cache[tmp_set][1][`CACHE_FULL_LINE_SIZE - 1] == 1'b0) begin
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][1][shift + i] = tmp_data2[j];   
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                        num_of_line = 1;
                    end else if (cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 3] == 1'b1) begin
                        // write back if needed
                        if (cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 2] == 1'b1) begin
                            #1;
                            b2_command = `C2_WRITE_LINE;
                            tmp_addr2[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE] = cache[tmp_set][0][`CACHE_LINE_SIZE + `CACHE_TAG_SIZE - 1 : `CACHE_LINE_SIZE];
                            tmp_addr2[`CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE - 1 : `CACHE_OFFSET_SIZE] = tmp_set;
                            b2_addr = tmp_addr2;

                            if (cnt < 3 + 8) begin
                                vnt = 15;
                                for (i = 0; i < 16; i += 1) begin
                                    tmp_data2[vnt] = cache[tmp_set][0][i + shift];
                                    vnt -= 1;
                                    #2;
                                end
                                shift += 16;
                            end
                            b2_command = 2'bz;
                            #1;
                            wait(b2_command == `C2_RESPONSE);
                        end
                        shift = 0;
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][0][shift + i] = tmp_data2[j];   
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                        num_of_line = 0;
                    end else begin
                        // write back if needed
                        // write back if needed
                        if (cache[tmp_set][1][`CACHE_FULL_LINE_SIZE - 2] == 1'b1) begin
                            #1;
                            b2_command = `C2_WRITE_LINE;
                            tmp_addr2[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE] = cache[tmp_set][0][`CACHE_LINE_SIZE + `CACHE_TAG_SIZE - 1 : `CACHE_LINE_SIZE];
                            tmp_addr2[`CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE - 1 : `CACHE_OFFSET_SIZE] = tmp_set;
                            b2_addr = tmp_addr2;

                            if (cnt < 3 + 8) begin
                                vnt = 15;
                                for (i = 0; i < 16; i += 1) begin
                                    tmp_data2[vnt] = cache[tmp_set][1][i + shift];
                                    vnt -= 1;
                                    #2;
                                end
                                shift += 16;
                            end
                            b2_command = 2'bz;
                            #1;
                            wait(b2_command == `C2_RESPONSE);
                        end
                        shift = 0;
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][1][shift + i] = tmp_data2[j];
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                        num_of_line = 1;
                    end
                end
                #1;
                b1_command = `C1_RESPONSE;
                vnt = 15;
                for (i = 0; i < 16; i++) begin
                    tmp_data1[vnt] = cache[tmp_set][num_of_line][tmp_addr[`CACHE_OFFSET_SIZE - 1 : 0] * 8 + i];
                end
                #2
                b1_data = tmp_data1;
                vnt = 15;
                for (i = 0; i < 16; i++) begin
                    tmp_data1[vnt] = cache[tmp_set][num_of_line][tmp_addr[`CACHE_OFFSET_SIZE - 1 : 0] * 8 + i];
                end
                b1_data = tmp_data1;
                #1;
                b1_command = 3'bz;
            end 
            cnt = 0;
        end

        (`C1_WRITE8) : begin
            if (cnt == 0) begin
                tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE] = a1_bus;
                tmp_data1 = b1_data;
                j = 0;
                shift = 0;
                for (i = 15; i >= 0; i -= 1) begin
                    tmp_buffer[shift + j] = tmp_data1[i];
                    j += 1;
                end
                shift += 16;
                cnt += 1;
            end if (cnt == 1) begin
                tmp_addr[`CACHE_OFFSET_SIZE - 1 : 0] = a1_bus; // mb bug
                cnt += 1;
            end else begin
                // find cacheLine
                if (cnt == 2) begin
                    tmp_set = tmp_addr[`CACHE_ADDR_SIZE - 1 - `CACHE_TAG_SIZE : `CACHE_OFFSET_SIZE];
                    if (
                        cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 4 : `CACHE_LINE_SIZE] == 
                        tmp_addr[`CACHE_ADDR_SIZE - `CACHE_TAG_SIZE - 1: `CACHE_OFFSET_SIZE]
                    ) begin
                        cache_hit = 1;
                    end

                    if (
                        cache[tmp_set][1][`CACHE_FULL_LINE_SIZE - 4 : `CACHE_LINE_SIZE] == 
                        tmp_addr[`CACHE_ADDR_SIZE - `CACHE_TAG_SIZE - 1: `CACHE_OFFSET_SIZE]
                    ) begin
                        cache_hit = 2;
                    end
                    cnt += 1;
                end
                // cache_hit
                if (cache_hit != 0) begin
                    // cache_hit += 1;
                    #(`CACHE_HIT - 1);
                    hit++;
                    shift = tmp_addr[`CACHE_OFFSET_SIZE : 0];
                    j = `CACHE_LINE_SIZE - 1;
                    for (vnt = 0; vnt < 2; vnt += 1) begin
                        for (i = 0; i < 16; i += 1) begin
                            cache[tmp_set][cache_hit - 1][shift + i] = tmp_buffer[j];    
                            j -= 1; 
                        end
                        #2;
                        shift -= 16;
                    end
                    cnt += 1;
                    #1;
                    b1_command = `C1_RESPONSE;
                    #1;
                    b1_command = 3'bz;
                end else begin
                    #(`CACHE_MISS);
                    // go to mem
                    miss++;
                    if (cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 1] == 1'b0) begin
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][0][shift + i] = tmp_data2[j];   
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                    end else if (cache[tmp_set][1][`CACHE_FULL_LINE_SIZE - 1] == 1'b0) begin
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][1][shift + i] = tmp_data2[j];    
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                        num_of_line = 1;
                    end else if (cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 3] == 1'b1) begin
                        // write back if needed
                        if (cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 2] == 1'b1) begin
                            #1;
                            b2_command = `C2_WRITE_LINE;
                            tmp_addr2[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE] = cache[tmp_set][0][`CACHE_LINE_SIZE + `CACHE_TAG_SIZE - 1 : `CACHE_LINE_SIZE];
                            tmp_addr2[`CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE - 1 : `CACHE_OFFSET_SIZE] = tmp_set;
                            b2_addr = tmp_addr2;

                            if (cnt < 3 + 8) begin
                                vnt = 15;
                                for (i = 0; i < 16; i += 1) begin
                                    tmp_data2[vnt] = cache[tmp_set][0][i + shift];
                                    vnt -= 1;
                                    #2;
                                end
                                shift += 16;
                            end
                            b2_command = 2'bz;
                            #1;
                            wait(b2_command == `C2_RESPONSE);
                        end
                        shift = 0;
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][0][shift + i] = tmp_data2[j];    
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                        num_of_line = 0;
                    end else begin
                        // write back if needed
                        // write back if needed
                        if (cache[tmp_set][1][`CACHE_FULL_LINE_SIZE - 2] == 1'b1) begin
                            #1;
                            b2_command = `C2_WRITE_LINE;
                            tmp_addr2[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE] = cache[tmp_set][0][`CACHE_LINE_SIZE + `CACHE_TAG_SIZE - 1 : `CACHE_LINE_SIZE];
                            tmp_addr2[`CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE - 1 : `CACHE_OFFSET_SIZE] = tmp_set;
                            b2_addr = tmp_addr2;

                            if (cnt < 3 + 8) begin
                                vnt = 15;
                                for (i = 0; i < 16; i += 1) begin
                                    tmp_data2[vnt] = cache[tmp_set][1][i + shift];
                                    vnt -= 1;
                                    #2;
                                end
                                shift += 16;
                            end
                            b2_command = 2'bz;
                            #1;
                            wait(b2_command == `C2_RESPONSE);
                        end
                        shift = 0;
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][1][shift + i] = tmp_data2[j];    
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                        num_of_line = 1;
                    end
                end

                shift = tmp_addr[`CACHE_OFFSET_SIZE : 0];
                j = `CACHE_LINE_SIZE - 1;
                for (vnt = 0; vnt < 1; vnt += 1) begin
                    for (i = 0; i < 16; i += 1) begin
                        cache[tmp_set][num_of_line][shift + i] = tmp_buffer[j];    
                        j -= 1; 
                        #2;
                    end
                    shift -= 16;
                end
                #1;
                b1_command = `C1_RESPONSE;
                #1;
                b1_command = 3'bz;
            end 
            cnt = 0;
        end

        (`C1_WRITE16) : begin
            if (cnt == 0) begin
                tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE] = a1_bus;
                tmp_data1 = b1_data;
                j = 0;
                shift = 0;
                for (i = 15; i >= 0; i -= 1) begin
                    tmp_buffer[shift + j] = tmp_data1[i];
                    j += 1;
                end
                shift += 16;
                cnt += 1;
            end if (cnt == 1) begin
                tmp_addr[`CACHE_OFFSET_SIZE - 1 : 0] = a1_bus; // mb bug
                cnt += 1;
            end else begin
                // find cacheLine
                if (cnt == 2) begin
                    tmp_set = tmp_addr[`CACHE_ADDR_SIZE - 1 - `CACHE_TAG_SIZE : `CACHE_OFFSET_SIZE];
                    if (
                        cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 4 : `CACHE_LINE_SIZE] == 
                        tmp_addr[`CACHE_ADDR_SIZE - `CACHE_TAG_SIZE - 1: `CACHE_OFFSET_SIZE]
                    ) begin
                        cache_hit = 1;
                    end

                    if (
                        cache[tmp_set][1][`CACHE_FULL_LINE_SIZE - 4 : `CACHE_LINE_SIZE] == 
                        tmp_addr[`CACHE_ADDR_SIZE - `CACHE_TAG_SIZE - 1: `CACHE_OFFSET_SIZE]
                    ) begin
                        cache_hit = 2;
                    end
                    cnt += 1;
                end
                // cache_hit
                hit++;
                if (cache_hit != 0) begin
                    // cache_hit += 1;
                    #(`CACHE_HIT - 1);

                    shift = tmp_addr[`CACHE_OFFSET_SIZE : 0];
                    j = `CACHE_LINE_SIZE - 1;
                    for (vnt = 0; vnt < 2; vnt += 1) begin
                        for (i = 0; i < 16; i += 1) begin
                            cache[tmp_set][cache_hit - 1][shift + i] = tmp_buffer[j];    
                            j -= 1; 
                        end
                        #2;
                        shift -= 16;
                    end
                    cnt += 1;
                    #1;
                    b1_command = `C1_RESPONSE;
                    #1;
                    b1_command = 3'bz;
                end else begin
                    #(`CACHE_MISS);
                    // go to mem
                    miss++;
                    if (cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 1] == 1'b0) begin
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][0][shift + i] = tmp_data2[j];    
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                    end else if (cache[tmp_set][1][`CACHE_FULL_LINE_SIZE - 1] == 1'b0) begin
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][1][shift + i] = tmp_data2[j];    
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                        num_of_line = 1;
                    end else if (cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 3] == 1'b1) begin
                        // write back if needed
                        if (cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 2] == 1'b1) begin
                            #1;
                            b2_command = `C2_WRITE_LINE;
                            tmp_addr2[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE] = cache[tmp_set][0][`CACHE_LINE_SIZE + `CACHE_TAG_SIZE - 1 : `CACHE_LINE_SIZE];
                            tmp_addr2[`CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE - 1 : `CACHE_OFFSET_SIZE] = tmp_set;
                            b2_addr = tmp_addr2;

                            if (cnt < 3 + 8) begin
                                vnt = 15;
                                for (i = 0; i < 16; i += 1) begin
                                    tmp_data2[vnt] = cache[tmp_set][0][i + shift];
                                    vnt -= 1;
                                    #2;
                                end
                                shift += 16;
                            end
                            b2_command = 2'bz;
                            #1;
                            wait(b2_command == `C2_RESPONSE);
                        end
                        shift = 0;
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][0][shift + i] = tmp_data2[j];    
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                        num_of_line = 0;
                    end else begin
                        // write back if needed
                        // write back if needed
                        if (cache[tmp_set][1][`CACHE_FULL_LINE_SIZE - 2] == 1'b1) begin
                            #1;
                            b2_command = `C2_WRITE_LINE;
                            tmp_addr2[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE] = cache[tmp_set][0][`CACHE_LINE_SIZE + `CACHE_TAG_SIZE - 1 : `CACHE_LINE_SIZE];
                            tmp_addr2[`CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE - 1 : `CACHE_OFFSET_SIZE] = tmp_set;
                            b2_addr = tmp_addr2;

                            if (cnt < 3 + 8) begin
                                vnt = 15;
                                for (i = 0; i < 16; i += 1) begin
                                    tmp_data2[vnt] = cache[tmp_set][1][i + shift];
                                    vnt -= 1;
                                end
                                #2;
                                shift += 16;
                            end
                            b2_command = 2'bz;
                            #1;
                            wait(b2_command == `C2_RESPONSE);
                        end
                        shift = 0;
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][1][shift + i] = tmp_data2[j];  
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                        num_of_line = 1;
                    end
                end

                shift = tmp_addr[`CACHE_OFFSET_SIZE : 0];
                j = `CACHE_LINE_SIZE - 1;
                for (vnt = 0; vnt < 2; vnt += 1) begin
                    for (i = 0; i < 16; i += 1) begin
                        cache[tmp_set][num_of_line][shift + i] = tmp_buffer[j];    
                        j -= 1; 
                        #2;
                    end
                    shift -= 16;
                end
                #1;
                b1_command = `C1_RESPONSE;
                #1;
                b1_command = 3'bz;
            end 
            cnt = 0;
        end

        (`C1_WRITE32) : begin //must-have
            if (cnt == 0) begin
                tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE] = a1_bus;
                tmp_data1 = b1_data;
                j = 0;
                shift = 0;
                for (i = 15; i >= 0; i -= 1) begin
                    tmp_buffer[shift + j] = tmp_data1[i];
                    j += 1;
                end
                shift += 16;
                cnt += 1;
            end if (cnt == 1) begin
                tmp_addr[`CACHE_OFFSET_SIZE - 1 : 0] = a1_bus; // mb bug
                tmp_data1 = b1_data;
                j = 0;
                for (i = 15; i >= 0; i -= 1) begin
                    tmp_buffer[shift + j] = tmp_data1[i];
                    j += 1;
                end
                shift += 16;
                cnt += 1;
            end else begin
                // find cacheLine
                if (cnt == 2) begin
                    tmp_set = tmp_addr[`CACHE_ADDR_SIZE - 1 - `CACHE_TAG_SIZE : `CACHE_OFFSET_SIZE];
                    if (
                        cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 4 : `CACHE_LINE_SIZE] == 
                        tmp_addr[`CACHE_ADDR_SIZE - `CACHE_TAG_SIZE - 1: `CACHE_OFFSET_SIZE]
                    ) begin
                        cache_hit = 1;
                    end

                    if (
                        cache[tmp_set][1][`CACHE_FULL_LINE_SIZE - 4 : `CACHE_LINE_SIZE] == 
                        tmp_addr[`CACHE_ADDR_SIZE - `CACHE_TAG_SIZE - 1: `CACHE_OFFSET_SIZE]
                    ) begin
                        cache_hit = 2;
                    end
                    cnt += 1;
                end
                // cache_hit
                if (cache_hit != 0) begin
                    // cache_hit += 1;
                    hit++;
                    #(`CACHE_HIT - 1);

                    shift = tmp_addr[`CACHE_OFFSET_SIZE : 0];
                    j = `CACHE_LINE_SIZE - 1;
                    for (vnt = 0; vnt < 4; vnt += 1) begin
                        for (i = 0; i < 16; i += 1) begin
                            cache[tmp_set][cache_hit - 1][shift + i] = tmp_buffer[j];    
                            j -= 1; 
                            #2;
                        end
                        shift -= 16;
                    end
                    cnt += 1;
                    #1;
                    b1_command = `C1_RESPONSE;
                    #1;
                    b1_command = 3'bz;
                end else begin
                    #(`CACHE_MISS);
                    // go to mem
                    miss++;
                    if (cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 1] == 1'b0) begin
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][0][shift + i] = tmp_data2[j];    
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                    end else if (cache[tmp_set][1][`CACHE_FULL_LINE_SIZE - 1] == 1'b0) begin
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][1][shift + i] = tmp_data2[j];   
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                        num_of_line = 1;
                    end else if (cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 3] == 1'b1) begin
                        // write back if needed
                        if (cache[tmp_set][0][`CACHE_FULL_LINE_SIZE - 2] == 1'b1) begin
                            #1;
                            b2_command = `C2_WRITE_LINE;
                            tmp_addr2[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE] = cache[tmp_set][0][`CACHE_LINE_SIZE + `CACHE_TAG_SIZE - 1 : `CACHE_LINE_SIZE];
                            tmp_addr2[`CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE - 1 : `CACHE_OFFSET_SIZE] = tmp_set;
                            b2_addr = tmp_addr2;

                            if (cnt < 3 + 8) begin
                                vnt = 15;
                                for (i = 0; i < 16; i += 1) begin
                                    tmp_data2[vnt] = cache[tmp_set][0][i + shift];
                                    vnt -= 1;
                                    #2;
                                end
                                shift += 16;
                            end
                            b2_command = 2'bz;
                            #1;
                            wait(b2_command == `C2_RESPONSE);
                        end
                        shift = 0;
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][0][shift + i] = tmp_data2[j];    
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                        num_of_line = 0;
                    end else begin
                        // write back if needed
                        // write back if needed
                        if (cache[tmp_set][1][`CACHE_FULL_LINE_SIZE - 2] == 1'b1) begin
                            #1;
                            b2_command = `C2_WRITE_LINE;
                            tmp_addr2[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE] = cache[tmp_set][0][`CACHE_LINE_SIZE + `CACHE_TAG_SIZE - 1 : `CACHE_LINE_SIZE];
                            tmp_addr2[`CACHE_OFFSET_SIZE + `CACHE_SETS_SIZE - 1 : `CACHE_OFFSET_SIZE] = tmp_set;
                            b2_addr = tmp_addr2;

                            if (cnt < 3 + 8) begin
                                vnt = 15;
                                for (i = 0; i < 16; i += 1) begin
                                    tmp_data2[vnt] = cache[tmp_set][1][i + shift];
                                    vnt -= 1;
                                    #2;
                                end
                                shift += 16;
                            end
                            b2_command = 2'bz;
                            #1;
                            wait(b2_command == `C2_RESPONSE);
                        end
                        shift = 0;
                        #1;
                        b2_command = `C2_READ_LINE;
                        b2_addr = tmp_addr[`CACHE_ADDR_SIZE - 1 : `CACHE_OFFSET_SIZE];
                        #1;
                        b2_command = 2'bz;
                        b2_addr = 2'bz;
                        wait(b2_command == `C2_RESPONSE);
                        if (cnt < 3 + 8 + 8) begin
                            tmp_data2 = b2_data;
                            j = 15;
                            for (i = 0; i < 16; i++) begin
                                cache[tmp_set][1][shift + i] = tmp_data2[j];    
                                j -= 1; 
                            end
                            shift += 16;
                            cnt += 1;
                        end
                        num_of_line = 1;
                    end
                end

                shift = tmp_addr[`CACHE_OFFSET_SIZE : 0];
                j = `CACHE_LINE_SIZE - 1;
                for (vnt = 0; vnt < 4; vnt += 1) begin
                    for (i = 0; i < 16; i += 1) begin
                        cache[tmp_set][num_of_line][shift + i] = tmp_buffer[j];    
                        j -= 1; 
                        #2;
                    end
                    shift -= 16;
                end
                #1;
                b1_command = `C1_RESPONSE;
                #1;
                b1_command = 3'bz;
            end 
            cnt = 0;
        end
    endcase
end

endmodule