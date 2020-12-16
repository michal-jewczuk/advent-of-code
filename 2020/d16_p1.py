import utils

def extract_rules(input_data):
    rule_lines = [] 
    for line in input_data:
        if line.isspace(): 
            break
        rule_lines.append(line)

    rules = {}

    for rule in rule_lines:
        name, ranges = extract_rule_data(rule)
        rules.update({name: ranges})

    return rules

def extract_rule_data(rule_line):
    name_val = rule_line.split(':')
    name = name_val[0]
    ranges_line = name_val[1].split('or')

    ranges = []
    for range in ranges_line:
        edges = range.split('-')
        ranges.append((int(edges[0].strip()), int(edges[1].strip())))

    return name, ranges

def extract_nearby_tickets(input_data):
    is_ticket = False
    tickets = []

    for line in input_data:
        if line.startswith('nearby'):
            is_ticket = True
            continue

        if is_ticket:
            tickets.append(line)

    return [extract_ticket_values(ticket) for ticket in tickets] 

def extract_ticket_values(ticket_line):
    return [int(value.strip()) for value in ticket_line.split(',')]

def is_value_in_range(rng, value):
    return (value >= rng[0] and value <= rng[1]) 

def is_value_in_any_range(ranges, value):
    for rng in ranges:
        if is_value_in_range(rng, value):
            return True

    return False

def get_ticket_invalid_numbers(ranges, ticket):
    invalid_val = []
    for value in ticket:
        if not is_value_in_any_range(ranges, value):
            invalid_val.append(value)

    return invalid_val 

def get_invalid_numbers(rules, tickets):
    invalid_numbers = []
    ranges = []
    for range in rules.values():
        ranges.extend(range)

    for ticket in tickets:
        invalid_numbers.extend(get_ticket_invalid_numbers(ranges, ticket))

    return invalid_numbers 

def get_sum_of_invalid_numbers(numbers):
    sum = 0
    for number in numbers:
        sum += number

    return sum

def solved16(input_data):
    rules = extract_rules(input_data)
    tickets = extract_nearby_tickets(input_data)
    invalid_numbers = get_invalid_numbers(rules, tickets)
    print(invalid_numbers)

    return get_sum_of_invalid_numbers(invalid_numbers) 

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d16_example.txt")
    real_data = utils.loadStringData("./data/d16_real.txt")

    test = solved16(test_data)
    real = solved16(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
