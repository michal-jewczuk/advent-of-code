import utils
import re

REQ_FIELDS = ['byr', 'ecl', 'eyr', 'hcl', 'hgt', 'iyr', 'pid']

def transform_input_data(input_data):
    data = []
    passport_line = ''

    for line in input_data:
        if line.isspace():
           data.append(passport_line.lstrip())
           passport_line = ''
        else:
           passport_line += ' ' + line.strip()

    data.append(passport_line.lstrip())

    return data

def extract_keys(passport):

    line = []
    key_values = passport.split(' ')

    for key_val in key_values:
        pieces = key_val.split(':')
        line.append(pieces[0])

    return line

def are_keys_valid(keys_entry):
    keys_entry.sort()

    if keys_entry.count('cid') > 0:
        keys_entry.remove('cid')

    return keys_entry == REQ_FIELDS

def validate_keys(passport):
    keys = extract_keys(passport)
    return are_keys_valid(keys)

def is_byr_valid(val):
    byr = int(val)
    return (byr >= 1920 and byr <= 2002) 

def is_iyr_valid(val):
    iyr = int(val)
    return (iyr >= 2010 and iyr <= 2020) 

def is_eyr_valid(val):
    eyr = int(val)
    return (eyr >= 2020 and eyr <= 2030) 

def is_ecl_valid(val):
    valid_ecls = {'amb', 'blu', 'brn', 'gry', 'grn', 'hzl', 'oth'}
    ecl = set()
    ecl.add(val)
    return ecl.issubset(valid_ecls)

def is_pid_valid(val):
    expr = re.compile("\d{9}$")
    result = expr.match(val)
    return result != None 

def is_hcl_valid(val):
    expr = re.compile("#[0-9,a-f]{6}$")
    result = expr.match(val)
    return result != None

def is_hgt_valid(val):
    if val.endswith("in"):
        boundries = (59, 76)
    elif val.endswith("cm"):
        boundries = (150, 193)
    else:
        return False
    hgt = int(val[:-2])

    return (hgt >= boundries[0] and hgt <= boundries[1]) 

def validate_element(element):
    [key,val] = element.split(':')

    if key == 'byr':
        if not is_byr_valid(val):
            return False
    elif key == 'iyr':
        if not is_iyr_valid(val):
            return False
    elif key == 'eyr':
        if not is_eyr_valid(val):
            return False
    elif key == 'ecl':
        if not is_ecl_valid(val):
            return False
    elif key == 'pid':
        if not is_pid_valid(val):
            return False
    elif key == 'hcl':
        if not is_hcl_valid(val):
            return False
    elif key == 'hgt':
        if not is_hgt_valid(val):
            return False

    return True
    

def is_passport_valid(passport):

    if not validate_keys(passport):
        return False

    elements = passport.split(' ')
    for element in elements:
        if not validate_element(element):
            return False

    return True 

def solved04(input_data):
    count = 0

    passports = transform_input_data(input_data)
    for passport in passports:
        if is_passport_valid(passport):
            count += 1

    return count 

if __name__ == '__main__':
    valid_test_data = utils.loadStringData("./data/d04_example_p2_valid.txt")
    invalid_test_data = utils.loadStringData("./data/d04_example_p2_invalid.txt")
    real_data = utils.loadStringData("./data/d04_real.txt")

    valid = solved04(valid_test_data)
    invalid = solved04(invalid_test_data)
    real = solved04(real_data)

    print(utils.OUTPUT_STRING.format("example invalid", invalid))
    print(utils.OUTPUT_STRING.format("example valid", valid))
    print(utils.OUTPUT_STRING.format("exercise", real))
