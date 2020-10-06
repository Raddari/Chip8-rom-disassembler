# Chip8-rom-disassembler
Disassembler for Chip8 binary files  

### Usage:
    java -jar Chip8-rom-disassembler.jar <rom-path>

Alternatively, write a batch script which allows you to drag and drop the file, such as:

    java -jar Chip8-rom-disassembler.jar %1

### Todo:
  - [x] Implement disassembler
  - [x] Add Command Line Interface
  - [x] Add output formatting
    - [x] With ~~YAML~~ JSON configuration
    - [ ] Allow custom formatting file
  - [x] Save output to file
  - [ ] Labels for memory addresses
  - [ ] Documentation
  - [ ] QoL?
