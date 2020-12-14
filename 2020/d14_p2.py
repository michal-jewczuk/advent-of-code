import utils
import re

def get_mask(line):
    tmp = line.split('=')
    string_mask = tmp[1].strip()
    mask = []

    for idx in range(-1, -37, -1):
        mask.append((idx, string_mask[idx]))

    return mask 

def get_bin_as_list(binary):
    initial = ['0' for el in range(0, 36)]
    binary = str(binary)[2:]
    limit = -1 * (len(binary) + 1)
    for idx in range (-1, limit, -1):
        initial[idx] = binary[idx]

    return initial

def apply_mask(binary, mask):
    bin_list = get_bin_as_list(binary)
    for entry in mask:
        bin_list[entry[0]] = entry[1]

    return bin_list

def convert_bin_to_int(binary):
    result = ''
    for el in binary:
        result += str(el)

    return int(result, 2) 

def get_line_info(line):
    tmp = line.split("=")
    tmp1 = tmp[0].split('[')
    mem_address = int(tmp1[1].strip()[:-1])
    value = int(tmp[1].strip())

    return mem_address, value

def get_all_mem_addresses(wildcard):
    all_addr = []
    partials = wildcard

    for idx in range(len(wildcard)):
        if wildcard[idx] == 'X':
            p1 = wildcard.copy()
            p2 = wildcard.copy()
            p1[idx] = '0' 
            p2[idx] = '1'

    return all_addr

def write_in_memory(line, mask, memory):
    mem_address, value = get_line_info(line)
    wildcard_address = apply_mask(bin(mem_address), mask)
    possible_addresses = get_all_mem_addresses(wildcard_address)
    print(possible_addresses)
    

def run_programm(input_data):
    current_mask =[]
    memory = {}

    for line in input_data:
        if line.startswith('mask'):
            current_mask = get_mask(line)
        else:
          write_in_memory(line, current_mask, memory)

    return memory

def calculate_result(memory):
    result = 0
    for value in memory.values():
        result += value

    return result


def solved14(input_data):
    memory = run_programm(input_data)

    return calculate_result(memory)

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d14_example1.txt")
    real_data = utils.loadStringData("./data/d14_real.txt")

    test = solved14(test_data)
    #real = solved14(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    #print(utils.OUTPUT_STRING.format("exercise", real))
