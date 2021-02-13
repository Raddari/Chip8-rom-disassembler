# Chip8-rom-disassembler
Disassembler for Chip8 binary files  

### Usage:
#### Disassembling
    java -jar Chip8-rom-disassembler.jar <rom-path>

Alternatively, write a batch script which allows you to drag and drop the file, such as:

    java -jar Chip8-rom-disassembler.jar %1
    
#### Configuration/Formatting
Create a file named `config.json` in the same directory as the program, or copy it from `assets/config/default.json`.  
The keys in the config file are all the valid options supported (currently just formatting options). All of them are pretty self explanatory.

### Todo:
  - [x] Implement disassembler
  - [x] Add Command Line Interface
  - [x] Add output formatting
    - [x] With ~~YAML~~ JSON configuration
    - [x] Allow custom formatting file
  - [x] Save output to file
  - [x] Labels for memory addresses
  - [ ] Default config values
  - [ ] Documentation
  - [ ] QoL?
  - [ ] GUI?
