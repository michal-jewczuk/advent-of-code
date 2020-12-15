import utils

def say_number(spoken, number, turn):
    value = spoken.get(number)

    if value == None:
        value = [turn]
    else:
        value.append(turn)

    spoken.update({number: value})

def was_number_spoken_twice_before(spoken, number):
    value = spoken.get(number)

    return not (value == None or len(value) < 2)

def get_next_number_to_speak(spoken, number):
    value = spoken.get(number)

    return value[-1] - value[-2]

def play_game(numbers, limit):
    turn = 0 
    spoken = {}
    last_spoken = -1

    while (turn < limit):
        turn += 1
        if (turn <= len(numbers)):
            last_spoken = numbers[turn - 1]
            say_number(spoken, last_spoken, turn)
            continue

        if was_number_spoken_twice_before(spoken, last_spoken):
            last_spoken = get_next_number_to_speak(spoken, last_spoken) 
        else:
            last_spoken = 0

        say_number(spoken, last_spoken, turn)

    return last_spoken

def solved15(input_data, limit):
    for numbers in input_data:
        print(numbers, '=>' , play_game(numbers, limit))

if __name__ == '__main__':
    input_data_1 = [[0,3,6], [1,3,2], [2,1,3], [1,2,3], [2,3,1], [3,2,1], [3,1,2], [11,18,0,20,1,7,16]]
    input_data_2 = [[0,3,6], [11,18,0,20,1,7,16]]
    print('======= LIMIT 2020 =============')
    solved15(input_data_1, 2020)
    print('======= LIMIT 30000000 =============')
    solved15(input_data_2, 30000000)
