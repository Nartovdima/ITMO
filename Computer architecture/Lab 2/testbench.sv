`include "cache.sv"

module mem_tb;

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

reg [14: 0] addr1_bus; 
reg [15: 0] data1_bus;
reg [2: 0] command1_bus;
reg [14: 0] addr2_bus; 
reg [15: 0] data2_bus;
reg [1: 0] command2_bus;

reg[1 : 0] CLK = 0;
reg [1 : 0] RESET = 0;
reg [1 : 0] C_DUMP = 0;

wire c1_bus;
wire d1_bus;
wire a1_bus;
wire c2_bus;
wire d2_bus;
wire a2_bus;

wire clk;
wire reset;
wire c_dump;
wire [31 : 0] hit;
wire [31 : 0] miss;

assign a1_bus = addr1_bus;
assign d1_bus = data1_bus;
assign c1_bus = command1_bus;

assign a2_bus = addr2_bus;
assign d2_bus = data2_bus;
assign c2_bus = command2_bus;

assign clk = CLK;
assign reset = RESET;
assign c_dump = C_DUMP;

reg [31 : 0] pa;
reg [31 : 0] pb;
reg [31 : 0] pc;

reg [7 : 0] a;
reg [15 : 0] b;
reg [31 : 0] c;

integer i = 0;
integer j = 0;
integer k = 0;

cache _cache(
    clk,
    c_dump,
    reset,

    a1_bus,
    d1_bus,
    c1_bus,

    a2_bus,
    d2_bus,
    c2_bus,

    hit,
    miss
);

initial begin
    addr1_bus [14 : 0] = 14'bz;
    addr2_bus [14 : 0] =  14'bz;
    command1_bus [2 : 0] = `C1_NOP;
    command2_bus [1 : 0] = `C2_NOP;
    data1_bus [15 : 0] = 15'bz;
    data2_bus [15 : 0] = 15'bz;
    pa = 32'd0;
    pb = 32'd2048;
    pc = 32'd3969;

    for (i = 0; i < 64; i++) begin
        for (j = 0; j < 60; j++) begin
            for (k = 0; k < 32; k++) begin
                #1;
                command1_bus = `C1_READ8;
                addr1_bus = (pa + k);
                #1;
                command1_bus = 3'bz;
                wait(command1_bus == `C1_RESPONSE);
                a = data1_bus;
                #1;

                #1;
                command1_bus = `C1_READ16;
                addr1_bus = (pb + 2 * j);
                #1;
                command1_bus = 3'bz;
                wait(command1_bus == `C1_RESPONSE);
                b [7 : 0] = data1_bus;
                #2;
                b [15 : 8] = data1_bus; 
                #1;

                c = a * b;

            end
            #1;
            command1_bus = `C1_WRITE32;
            addr1_bus = (pc + 4 * j);
            #1;
            command1_bus = 3'bz;
            wait(command1_bus == `C1_RESPONSE);
            #1;
        end
    end

    $display("cache_hits = %d, cache_mises = %d", hit, miss);
    $display("tacts = %d", $time / 2);
    $finish;
end

always #1 begin
    CLK = ~CLK;
end

endmodule