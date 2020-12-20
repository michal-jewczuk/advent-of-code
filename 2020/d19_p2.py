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

def get_part_matching_rule_42(message):
    rule42 = DECODED.get('42')
    longest_match = '' 
    for rule in rule42:
        match = get_longest_match(rule, message)
        if len(match) > len(longest_match):
            longest_match = match

    return longest_match

def does_it_start_with(rules, message):
    for rule in rules:
        if message.startswith(rule):
            return True

    return False

def get_set_item_length(items):
    copy = items.copy()
    tmp = copy.pop()
    return len(tmp)

def get_combined_length(rule1, rule2):
    return get_set_item_length(rule1) + get_set_item_length(rule2) 

def does_it_match_part_1(message, rule42, rule31):
    combined = [r42 + r31 for r42 in rule42 for r31 in rule31]
    return combined.count(message) > 0    

def does_it_match_rule_31x2(message, rule31):
    combined = [r311 + r312 for r311 in rule31 for r312 in rule31]
    return combined.count(message) > 0

def does_it_match_part_2(message, rule42, rule31):
    valid_42 = get_part_matching_rule_42(message)
    if len(valid_42) == 0:
        return False
    message = message[len(valid_42):]
    len31 = get_set_item_length(rule31) * 2

    if len(message) != len31:
        return False

    return does_it_match_rule_31x2(message, rule31)

def is_rest_valid_for_rule_11(message):
    # 42 31 | 42 11 31
    rule42 = DECODED.get('42')
    rule31 = DECODED.get('31')

    length =  get_combined_length(rule42, rule31)
    print(len(message), length)
    if len(message) < length: 
        return False
    elif len(message) == length:
        return does_it_match_part_1(message, rule42, rule31)
    else:
        return does_it_match_part_2(message, rule42, rule31)

    return False

def is_valid(message):
    valid_8 = get_part_matching_rule_42(message)
    if len(valid_8) == 0:
        return False

    message = message[len(valid_8):]
    return is_rest_valid_for_rule_11(message)
    
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
    #real_data = utils.loadStringData("./data/d19_real2.txt")

    e2r = solved19(e2)
    #e3r = solved19(e3)
    #real = solved19(real_data)

    print(utils.OUTPUT_STRING.format("example 2", e2r))
    #print(utils.OUTPUT_STRING.format("example 3", e3r))
    #print(utils.OUTPUT_STRING.format("exercise", real))
