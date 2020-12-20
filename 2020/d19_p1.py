import utils

DECODED = {}

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

def is_valid(message):
    variants = DECODED.get('0')
    try: 
        variants.remove(message)
        return True
    except:
        return False

def count_matching(messages):
    count = 0
    for message in messages:
        if is_valid(message):
            count += 1

    return count

def solved19(input_data):
    DECODED.clear()
    rules = extract_rules(input_data)
    decode_rules(rules)
    messages = extract_messages(input_data)

    return count_matching(messages) 

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d19_example.txt")
    real_data = utils.loadStringData("./data/d19_real.txt")

    test = solved19(test_data)
    real = solved19(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
