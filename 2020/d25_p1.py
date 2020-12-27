import utils

def get_loop_size(key):
    value = 1
    multi = 7
    divider = 20201227
    counter = 1

    while True:
        value = (value * multi) % divider
        if value  == key:
            return counter
        counter += 1

def transform_key(key, counter):
    value = 1
    divider = 20201227
    for __i in range(counter):
        value = (value * key) % divider

    return value

def solved25(doorpk, cardpk):
    doorloop = get_loop_size(doorpk)
    cardloop = get_loop_size(cardpk)
    doorkey = transform_key(cardpk, doorloop)
    cardkey = transform_key(doorpk, cardloop)

    if doorkey == cardkey:
        return doorkey
    else:
        return -1

if __name__ == '__main__':

    test = solved25(17807724,5764801)
    real = solved25(4738476, 3248366)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
