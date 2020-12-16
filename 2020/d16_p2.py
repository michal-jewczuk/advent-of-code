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

def extract_my_ticket(input_data):
    is_ticket = False
    for line in input_data:
        if line.startswith('your'):
            is_ticket = True
            continue
        if is_ticket:
            return extract_ticket_values(line)

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

def is_ticket_valid(rules, ticket):
    ranges = []
    for range in rules.values():
        ranges.extend(range)

    return len(get_ticket_invalid_numbers(ranges, ticket)) == 0

def get_valid_tickets(rules, tickets):
    valid_tickets = []
    for ticket in tickets:
        if is_ticket_valid(rules, ticket):
            valid_tickets.append(ticket)

    return valid_tickets

def get_ticket_values_at_pos(tickets, pos):
    return [ticket[pos] for ticket in tickets]

def are_all_values_valid_for_ranges(ranges, values):
    for value in values:
        if not is_value_in_any_range(ranges, value):
            return False

    return True

def get_rule_positions(ranges, tickets):
    all_pos = []
    for pos in range(len(tickets[0])):
        values_at_pos = get_ticket_values_at_pos(tickets, pos)
        if are_all_values_valid_for_ranges(ranges, values_at_pos):
            all_pos.append(pos) 

    return all_pos 

def determine_all_possible_places(rules, tickets):
    order = {}

    for rule in rules.keys():
        ranges = rules.get(rule)
        idxs= get_rule_positions(ranges, tickets)
        order.update({rule: idxs})

    return order

def find_key_with_one_value(orders, keys):
    for key in keys:
        if len(orders[key]) == 1:
            return key

    return None

def get_values_without(values, value):
    copy = values.copy()
    try:
        idx = values.index(value)
        copy.pop(idx)
        return copy
    except:
        print('not found', value)
        return copy

def remove_single_value(orders, key):
    val_to_rmv = orders[key][0]
    orders.pop(key)
    for key in orders.keys():
        vals_without = get_values_without(orders[key], val_to_rmv)
        orders.update({key: vals_without})

def eliminate_duplicates(all_orders):
    keys = list(all_orders.keys())
    orders = all_orders.copy()
    singles = {}
    while len(keys) > 0:
        single = find_key_with_one_value(orders, keys)
        if single == None:
            print('oops', orders)
            break
        else:
            singles.update({single: orders[single][0]})
            remove_single_value(orders, single)
            keys.remove(single)

    return singles 

def determine_field_order(rules, tickets):
    all_orders = determine_all_possible_places(rules, tickets)
    order = eliminate_duplicates(all_orders)

    return order

def calculate_result(order, my_ticket):
    result = 1
    for key in order.keys():
        if key.startswith('departure'):
            result *= my_ticket[order[key]]

    return result

def solved16(input_data):
    rules = extract_rules(input_data)
    tickets = extract_nearby_tickets(input_data)
    valid_tickets = get_valid_tickets(rules, tickets)
    my_ticket = extract_my_ticket(input_data)
    valid_tickets.append(my_ticket)
    field_order = determine_field_order(rules, valid_tickets)

    print(field_order)
    return calculate_result(field_order, my_ticket)

if __name__ == '__main__':
    #test_data = utils.loadStringData("./data/d16_example2.txt")
    real_data = utils.loadStringData("./data/d16_real.txt")

    #test = solved16(test_data)
    real = solved16(real_data)

    #print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
