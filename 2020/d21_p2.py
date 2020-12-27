import utils

def extract_ings(lines):
    ings = {} 
    for line in lines:
        tmp = line.split('(contains')
        ings_ = tmp[0].strip().split(' ')
        for ing in ings_:
            count = ings.get(ing,0) + 1
            ings.update({ing:count})

    return ings

def get_singles(allergens):
    singles = []
    for key in allergens.keys():
        tmp = allergens.get(key)
        if len(tmp) == 1:
            singles.append(list(tmp)[0])

    return singles

def remove_singles(allergens, singles):
    for single in singles:
        for key in allergens.keys():
            tmp = allergens.get(key)
            if len(tmp) == 1:
                continue
            tmp.discard(single)
            allergens.update({key: tmp})

def get_allergen_ings(lines):
    allergens = {}
    for line in lines:
        tmp = line.split('(contains')
        keys = tmp[1].strip()[:-1].split(', ')
        ings = set(tmp[0].strip().split(' '))

        for key in keys:
            alg = allergens.get(key)
            if alg == None:
                allergens.update({key: ings})
            else:
                allergens.update({key: alg.intersection(ings)})

    singles = []
    while len(singles) < len(allergens):
        singles = get_singles(allergens)
        remove_singles(allergens, singles)

    return allergens

def create_canonical_list(allergens):
    keys = [key for key in allergens.keys()]
    keys.sort()
    return (',').join([list(allergens.get(key))[0] for key in keys])


def solved21(input_data):
    ings = extract_ings(input_data)
    allergens = get_allergen_ings(input_data)

    return create_canonical_list(allergens)

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d21_example.txt")
    real_data = utils.loadStringData("./data/d21_real.txt")

    test = solved21(test_data)
    real = solved21(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
