import utils

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

def extract_keys(passports):
    data = []

    for passport in passports:
        line = []
        key_values = passport.split(' ')

        for key_val in key_values:
            pieces = key_val.split(':')
            line.append(pieces[0])

        data.append(line)

    return data

def is_passport_valid(keys_entry):
    keys_entry.sort()

    if keys_entry.count('cid') > 0:
        keys_entry.remove('cid')

    return keys_entry == REQ_FIELDS


def solved04(input_data):
    count = 0

    transformed_data = transform_input_data(input_data)
    passport_keys = extract_keys(transformed_data)
    for keys_entry in passport_keys:
        if is_passport_valid(keys_entry):
            count += 1

    return count 

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d04_example.txt")
    real_data = utils.loadStringData("./data/d04_real.txt")

    test = solved04(test_data)
    real = solved04(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
