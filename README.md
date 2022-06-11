# **Stonks Exchange** - `a Minecraft SMP Bank Plugin`
![](https://github.com/TimEpsilon/Stonks-Exchange/blob/master/pack.png)

## *Introduction*
_Stonks Exchange_, anciennement datapack sous le nom de SMP-Bank, est le plugin de gestion d'économie utilisé sur *StonksLand 2.0 : New World, Old Wilds*.

Ce plugin repose principalement sur la monnaie officielle, le M-Coin, obtenable en interagissant avec un Vault Block. Contrairement à *StonksLand V1*, le M-Coin ne permet plus d'acheter et de vendre des ressources, les échanges étant à présent à la charge des joueurs, qui peuvent donc fixer leurs propres prix. L'idée est de permettre les interactions et la créativité, ainsi que d'éviter une situation de crash boursier avec juste une usine. Chaque joueur doit pouvoir partir sur un pied d'égalité.

## *La Banque*

Chaque joueur dispose d'un compte unique et privé, lié à leur UUID. Ce compte est accessible depuis un Vault Block. Ici 

L'interface se présente comme ceci :

//TODO screen interface

    - `player_head` : Affiche votre solde et votre solde max.
    - `nether_star` : Affiche le taux actuel.
    - `diamond` : Depose N diamants et M-Coins. Converti les diamants en leur équivalent en M-Coin.
    - `mcoin` : Retire N M-Coins
    - `wool` : Affiche votre rang et le prix du suivant.

Afin d'éviter toute fraude, chaque interaction est gardée en mémoire et consultable par l'administrateur dans `stonksexchange/logs`.

### Point Important

Il n'est pas possible de reconvertir des M-Coins en diamants, faîtes donc bien attention à ne pas convertir plus que nécessaire.

## *Le Taux*

Grande nouveauté sur StonksLand, le M-Coin est à présent une monnaie spéculative. Toutes les `8h`, un taux de conversion diamant -> M-Coins est calculé selon l'activité du serveur. Ce taux s'applique sur chaque diamant déposé dans la banque, le convertissant en un certain nombre de M-Coins. Les valeurs décimales jusqu'à `0.001 M-Coins` sont donc à présent possibles, mais seul un nombre entier de M-Coins peut être retiré physiquement.

### Détails Mathématiques

#### ***TL;DR*** 
4 paramètres influencent le taux, du plus influent au moins :
- Le nombre de joueurs connectés sur la période (+)
- Le nombre de morts sur la période (-)
- Le nombre de diamants minés (-)
- La quantité totale de M-Coins échangées à la banque (+) / (-) si bénéfice/déficit

#### Formules et Calculs

//TODO : maths
