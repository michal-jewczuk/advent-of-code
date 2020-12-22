import utils

def extract_decks(data):
    player1 = []
    player2 = []
    is_player1 = True

    for line in data:
        if line.startswith('Player'):
            continue
        if line.isspace():
            is_player1 = False
            continue

        if is_player1:
            player1.append(int(line.strip()))
        else:
            player2.append(int(line.strip()))

    return player1, player2

def play_round(player1, player2):
    card1 = player1.pop(0)
    card2 = player2.pop(0)
    if card1 > card2:
        player1.append(card1)
        player1.append(card2)
    elif card2 > card1:
        player2.append(card2)
        player2.append(card1)
    else:
        print('ooops tie!')

def is_a_winner(player1, player2):
    if len(player1) == 0:
        return player2
    elif len(player2) == 0:
        return player1
    else:
        return None

def play_game(player1, player2):
    rounds = 1

    while True:
        play_round(player1, player2)
        winner = is_a_winner(player1, player2)
        if winner != None:
            print('Rounds played: {}, winners deck: {}'.format(rounds, winner))
            break
        rounds += 1

    return winner

def calculate_score(winner):
    score = 0
    multi = len(winner)
    for ele in winner:
        score += multi * ele
        multi -= 1

    return score

def solved22(input_data):
    player1, player2 = extract_decks(input_data)
    winner = play_game(player1, player2)
    return calculate_score(winner) 

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d22_example.txt")
    real_data = utils.loadStringData("./data/d22_real.txt")

    test = solved22(test_data)
    real = solved22(real_data)

    print(utils.OUTPUT_STRING.format("example", test))
    print(utils.OUTPUT_STRING.format("exercise", real))
