import utils

DECODED = {}
SPECIAL = {}

def decode_string(rule):
    parts = rule.split(' ')
    variants = ['']

    for rule in parts:
        elements = DECODED.get(rule)
        variants = [var + el for var in variants for el in elements]

    return variants

def decode_simple(rule, key):
    tmp = rule.split('"')
    DECODED.update({key: set(tmp[1])})
    
def decode_single_rule(rule, key):
    if rule.find('"') != -1:
        decode_simple(rule, key)
        return

    parts = rule.split('|')
    rules = []
    for part in parts:
        tmp = decode_string(part.strip())
        rules.extend(tmp)

    DECODED.update({key: set(rules)})

def is_rule_decodable(rule, key):
    if rule.find('"') != -1:
        return True

    keys = rule.split(' ')
    for key in keys:
        if key == '|':
            continue
        if DECODED.get(key) == None:
            return False

    return True


def decode_rules(rules):
    i = 0
    while len(rules) > 0:
        keys_to_remove = []
        for key in rules.keys():
            rule = rules.get(key)
            if is_rule_decodable(rule, key):
                decode_single_rule(rule, key)
                keys_to_remove.append(key)

        for ktr in keys_to_remove:
            rules.pop(ktr)

def extract_rules(data):
    rules = {}
    for line in data:
        if line.isspace():
            return rules
        tmp = line.split(':')
        rules.update({tmp[0]: tmp[1].strip()})

    return rules

def remove_special_rules(rules):
    special_keys = ['0', '8', '11']
    for key in special_keys:
        SPECIAL.update({key: rules.get(key)})
        rules.pop(key)

def extract_messages(input_data):
    is_message = False
    messages = []
    for line in input_data:
        if line.isspace():
            is_message = True
            continue
        if is_message:
            messages.append(line.strip())

    return messages

def get_longest_match(rule, message):
    current = ''
    while True:
        if not message.startswith(current):
            return current
        else:
            current += rule


def does_it_start_with(rules, message):
    for rule in rules:
        if message.startswith(rule):
            return True

    return False

def are_rules_simple():
    idx8 = SPECIAL.get('8').find('|')
    idx11 = SPECIAL.get('11').find('|')

    return (idx8 == -1 and idx11 == -1)

def is_valid_for_simple(message):
    decode_single_rule(SPECIAL.get('8'), '8')
    decode_single_rule(SPECIAL.get('11'), '11')
    decode_single_rule(SPECIAL.get('0'), '0')
    variants = DECODED.get('0')
    try:
        variants.remove(message)
        return True
    except:
        return False

def get_matching_length(rules, message, start):
    for rule in rules:
        if start:
            if message.startswith(rule):
                return len(rule)
        else:
            if message.endswith(rule):
                return (-1 * len(rule))
    return 0

def get_length_of_rule(key):
    rule = DECODED.get(key)
    rule = list(rule)
    return len(rule[0])

def is_valid_for_multiple_4231(message):
    rule42 = DECODED.get('42')
    rule31 = DECODED.get('31')

    while len(message) != 0:
        len42 = get_matching_length(rule42, message, True)
        if len42 == 0:
            return False
        message = message[len42:]
        len31 = get_matching_length(rule31, message, False)
        if len31 == 0:
            return False
        message = message[:len31]
        if len(message) % (-1 * len31 + len42) != 0:
            return False

    return True

def get_times_matching_42(message):
    count = 0
    rule42 = DECODED.get('42')

    while len(message) != 0:
        len42 = get_matching_length(rule42, message, True)
        if len42 == 0:
            return count
        message = message[len42:]
        count += 1 

    return 0 

def is_valid_for_combined(message):
    rule42 = DECODED.get('42')
    rule31 = DECODED.get('31')
    len42 = get_length_of_rule('42') 
    times42 = get_times_matching_42(message)
    if times42 < 2:
        return False

    for i in range(times42):
        message = message[len42:]
        if is_valid_for_multiple_4231(message):
            return True

    return False

def is_valid(message):
    if are_rules_simple():
        return is_valid_for_simple(message)
    else:
        return is_valid_for_combined(message)
    
def count_matching(messages):
    count = 0
    for message in messages:
        if is_valid(message):
            count += 1

    return count

def solved19(input_data):
    DECODED.clear()
    rules = extract_rules(input_data)
    remove_special_rules(rules)
    decode_rules(rules)
    messages = extract_messages(input_data)

    return count_matching(messages) 

if __name__ == '__main__':
    e2 = utils.loadStringData("./data/d19_example2.txt")
    e3 = utils.loadStringData("./data/d19_example3.txt")
    real_data = utils.loadStringData("./data/d19_real2.txt")

    e2r = solved19(e2)
    e3r = solved19(e3)
    real = solved19(real_data)

    print(utils.OUTPUT_STRING.format("example 2", e2r))
    print(utils.OUTPUT_STRING.format("example 3", e3r))
    print(utils.OUTPUT_STRING.format("exercise", real))
