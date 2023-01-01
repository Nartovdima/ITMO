cmake_minimum_required(VERSION 3.23)
project(disAssembler)

set(CMAKE_CXX_STANDARD 20)

add_executable(disAssembler rv3.cpp elf_data.h)
