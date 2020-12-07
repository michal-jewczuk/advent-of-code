import utils

def extract_bag_name(line):
    tmp = line.split(' bags')
    return tmp[0]

def is_bag_in_rule(rule, bag):
    return rule.rfind(' ' + bag) != -1

def get_bags_containing(rules, bag):
    bags = set() 

    for rule in rules:
        if is_bag_in_rule(rule, bag):
            bags.add(extract_bag_name(rule))

    return bags


def solved07(input_data):
    count = 0
    MAIN_BAG = 'shiny gold'

    initial = get_bags_containing(input_data, MAIN_BAG)
    bags_to_check = initial.copy()

    while len(bags_to_check) > 0:
        bag = bags_to_check.pop()
        more_bags = get_bags_containing(input_data, bag)
        initial.update(more_bags)
        bags_to_check.update(more_bags)


    return len(initial) 

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d07_example.txt")
    real_data = utils.loadStringData("./data/d07_real.txt")

    test = solved07(test_data)
    real = solved07(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
