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

def was_this_round_played(current, past):
    for key in past.keys():
        rnd = past.get(key)
        if (current[0] == rnd[0] and current[1] == rnd[1]):
            return True

    return False 

def play_game(player1, player2):
    rounds = 1
    subgame = 1
    past_rounds = {}
    past_rounds.clear()

    while True:
        if was_this_round_played([player1, player2], past_rounds):
            #print('The same round - player 1 wins this one')
            return 1
        else:
            past_rounds.update({rounds: [player1.copy(), player2.copy()]})

        if len(player1) == 0:
            #print('Rounds played: {}, winners deck: {}'.format(rounds, player2))
            return 2
        elif len(player2) == 0:
            #print('Rounds played: {}, winners deck: {}'.format(rounds, player1))
            return 1

        card1 = player1.pop(0)
        card2 = player2.pop(0)

        if (card1 <= len(player1) and card2 <= len(player2)):
            subgame += 1
            #print('Submersing into subgame number {} of current game/subgame :)'.format(subgame))
            deck1 = [player1[el] for el in range(card1)]
            deck2 = [player2[el] for el in range(card2)]
            winnerSubgame = play_game(deck1, deck2)
            if winnerSubgame == 1:
                player1.append(card1)
                player1.append(card2)
            else:
                player2.append(card2)
                player2.append(card1)
        else:
            if card1 > card2:
                player1.append(card1)
                player1.append(card2)
            else:
                player2.append(card2)
                player2.append(card1)
        rounds += 1

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
    if winner == 1:
        winner = player1
    else:
        winner = player2
    return calculate_score(winner) 

if __name__ == '__main__':
    test_data = utils.loadStringData("./data/d22_example.txt")
    real_data = utils.loadStringData("./data/d22_real.txt")

    test = solved22(test_data)
    print(utils.OUTPUT_STRING.format("example", test))

    real = solved22(real_data)
    print(utils.OUTPUT_STRING.format("exercise", real))
