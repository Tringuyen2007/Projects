import random

def fight():
    # List of fighters
    fighters = ["rodtang", "jon jones", "du bronx", "conor mcgregor", "khalib nurmagomedov", "khamzat chimaev", "alex pierera", "dimitrius johnson", "mike tyson", "muhamad ali"] #list of fighters”


    print("Welcome to the octagon!")

    # Display the list of fighters
    print("Here are the list of fighters:")
    for abc, fighter in enumerate(fighters, start=1):
        print(f"{abc}. {fighter}")

    # Let the user pick a fighter
    while True:
    
        choice = (input("Enter the name of the fighter you would like to represent you: "))
        if choice.lower() in fighters:
            
            break
        else:
            print("Invalid fighter choice; please choose a fighter from the list.")
    else:
        print("Please enter a valid fighter")
        
       

    print(f"You chose {choice}")

    # Simulate matchups
    print("Here are your matchups:")
    wins = 0
    losses = 0

    for fighter in fighters:
        if fighter == choice.lower():
            continue

        print(f"{choice} vs {fighter}...", end=" ")
        result = random.choice(["win", "loss"])
        if result == "win":
            print("You won!")
            wins += 1
        else:
            print("You lost!")
            losses += 1

    # Display results
    print(f"\nResults for {choice}:")
    print(f"Wins: {wins}")
    print(f"Losses: {losses}")

    if wins > losses:
        print(f"Your fighter {choice} record is {wins} wins and {losses} losses, congratulations your fighter is the winner overall!")
    else:
        print(f"Your fighter{choice} record is {wins} wins and {losses} losses, better luck next time!")

if __name__ == "__main__":
    fight()


