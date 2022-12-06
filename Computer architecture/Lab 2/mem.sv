module mem(
    input wire CLK, 
    input wire M_DUMP, 
    input wire RESET, 

    input wire [14: 0] a2_bus, 
    inout wire [15: 0] d2_bus, 
    inout wire [1: 0] c2_bus
    );

`define ADDR2_BUS_SIZE 15
`define DATA2_BUS_SIZE 16
`define CTR2_BUS_SIZE 2
`define CACHE_TAG_SIZE 10
`define CACHE_SETS_SIZE 5
`define CACHE_ADDR_SIZE 19
`define CACHE_LINE_SIZE 128
`define CACHE_OFFSET_SIZE 4
`define MEM_SIZE 524288

`define MEM_WORKING_TIME 100

`define C2_NOP 2'b00
`define C2_READ_LINE 2'b10
`define C2_WRITE_LINE 2'b11
`define C2_RESPONSE 2'b01

reg [`ADDR2_BUS_SIZE - 1: 0] addr;
reg [`DATA2_BUS_SIZE - 1: 0] data;
reg [`CTR2_BUS_SIZE - 1: 0] command;

assign a2_bus = addr;
assign d2_bus = data;
assign c2_bus = command;

integer SEED = 225526;
reg[7:0] a[0:99];
reg [7 : 0] memory[0 : 524287];
integer i = 0;
integer k = 0;
integer j = 0;
integer cnt = 0;
reg [(`CACHE_TAG_SIZE + `CACHE_SETS_SIZE - 1) + 4 : 0] tmp_addr;
reg [`DATA2_BUS_SIZE - 1 : 0] tmp_data;
reg [`CACHE_OFFSET_SIZE - 1 : 0] offset = 4'b0000;
reg [7 : 0] new_a;

initial begin
    addr[`ADDR2_BUS_SIZE - 1: 0] = 15'bz;
    data[`DATA2_BUS_SIZE - 1: 0] = 16'bz;
    command[`CTR2_BUS_SIZE - 1: 0] = 2'bz;

    for (i = 0; i < `MEM_SIZE; i += 1) begin
        memory[i] = $random(SEED)>>16;  
    end
   
    /*for (i = 0; i < 100; i += 1) begin
        $display("[%d] %d", i, a[i]);  
    end*/
end

always @(RESET) begin
    addr[`ADDR2_BUS_SIZE - 1: 0] = 15'bz;
    data[`DATA2_BUS_SIZE - 1: 0] = 16'bz;
    command[`CTR2_BUS_SIZE - 1: 0] = 2'bz;

    for (i = 0; i < `MEM_SIZE; i += 1) begin
        memory[i] = $random(SEED)>>16;  
    end
   
    /*for (i = 0; i < 100; i += 1) begin
        $display("[%d] %d", i, a[i]);  
    end*/
end

always @(M_DUMP) begin
    $dumpfile("output.txt"); 
end


always @(posedge CLK) begin
    case(command)
        `C2_READ_LINE : begin //C2_READ_LINE
            tmp_addr[(`CACHE_TAG_SIZE + `CACHE_SETS_SIZE - 1)] = a2_bus;
            offset = 4'b1111;
            #(`MEM_WORKING_TIME * 2 - (`CACHE_LINE_SIZE / `DATA2_BUS_SIZE) * 2 - 1);
            command = `C2_RESPONSE;
            for (i = 0; i < (`CACHE_LINE_SIZE / `DATA2_BUS_SIZE); i += 1) begin
                new_a = memory[(tmp_addr << 4) + offset];
                offset -= 1;
                data[15 : 8] = new_a;
                new_a = memory[(tmp_addr << 4) + offset];
                offset -= 1;
                data[7 : 0] = new_a;
                #2;    
            end
            #1;
            command = 2'bz;
        end

        `C2_WRITE_LINE : begin //C2_WRITE_LINE
            if (cnt == 0) begin
                tmp_addr[(`CACHE_TAG_SIZE + `CACHE_SETS_SIZE - 1)] = a2_bus;
                offset = 4'b1111;
            end
            if (cnt < 8) begin
                tmp_data = d2_bus;
                memory[(tmp_addr << 4) + offset] = tmp_data[15 : 8];
                offset = offset - 4'b0001;
                memory[(tmp_addr << 4) + offset] = tmp_data[7 : 0];
                offset = offset - 4'b0001;
                cnt += 1;
            end
            if (cnt == 7) begin
                cnt = 0;
                #(`MEM_WORKING_TIME * 2 - (`CACHE_LINE_SIZE / `DATA2_BUS_SIZE) * 2 - 1);
                command = `C2_RESPONSE;
                #1;
                command = 2'bz;
            end
        end

    endcase
end
endmodule