import utils

def extract_bag_name(line):
    tmp = line.split(' bags')
    return tmp[0]

def get_bags(rule, bag):
    tmp = rule.split('contain')
    bags = tmp[1].strip().split(' bag')
    bags = clear_trash(bags)
    bags = transform_to_dict(bags)

    return bags

def clear_trash(bags):
    result = []
    for bag in bags:
        if bag.startswith('s.') or bag.startswith('no other') or bag.startswith('.'):
            continue
        elif bag.startswith('s,'):
            result.append(bag[3:])
        elif bag.startswith(','):
            result.append(bag[2:])
        else:
            result.append(bag)
    return result

def transform_to_dict(bags):
    result = {}
    for bag in bags:
        count = int(bag[0:2].strip())
        name = bag[2:].strip()
        result.update({name: count})

    return result

def get_children_count(all_bags, bag):
    children = all_bags.get(bag)

    if len(children) == 0:
        return 1
    else:
        count = 1 
        keys = children.keys()
        for key in keys:
            child_count = get_children_count(all_bags, key)
            count += children.get(key) * child_count
        return count 


def get_bags_containing(rules, bag):
    for rule in rules:
        if rule.startswith(bag):
            return get_bags(rule, bag)

    return {}

def extract_bags_tree(lines):
    all_bags = {}

    for line in lines:
        name = extract_bag_name(line)
        bags = get_bags_containing(lines, name)
        all_bags.update({name: bags})

    return all_bags

def do_children_have_children(all_bags, children):
    for child in children:
        if len(all_bags.get(child)) > 0:
            return True
    return False

def solved07(input_data):
    MAIN_BAG = 'shiny gold'
    all_bags = extract_bags_tree(input_data)
    count = get_children_count(all_bags, MAIN_BAG)

    return count - 1

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d07_example.txt")
    test_data2 = utils.loadStringData("./data/d07_example_2.txt")
    real_data = utils.loadStringData("./data/d07_real.txt")

    test = solved07(test_data)
    test2 = solved07(test_data2)
    real = solved07(real_data)

    print(utils.OUTPUT_STRING.format("example 1", test))
    print(utils.OUTPUT_STRING.format("example 2", test2))
    print(utils.OUTPUT_STRING.format("exercise", real))
