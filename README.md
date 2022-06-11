# **Stonks Exchange** - `a Minecraft SMP Bank Plugin`
![](https://github.com/TimEpsilon/Stonks-Exchange/blob/master/pack.png)

## *Introduction*
_Stonks Exchange_, anciennement datapack sous le nom de SMP-Bank, est le plugin de gestion d'économie utilisé sur *StonksLand 2.0 : New World, Old Wilds*.

Ce plugin repose principalement sur la monnaie officielle, le M-Coin, obtenable en interagissant avec un Vault Block. Contrairement à *StonksLand V1*, le M-Coin ne permet plus d'acheter et de vendre des ressources, les échanges étant à présent à la charge des joueurs, qui peuvent donc fixer leurs propres prix. L'idée est de permettre les interactions et la créativité, ainsi que d'éviter une situation de crash boursier avec juste une usine. Chaque joueur doit pouvoir partir sur un pied d'égalité.

## *La Banque*

Chaque joueur dispose d'un compte unique et privé, lié à leur UUID. Ce compte est accessible depuis un Vault Block. 
![](https://github.com/TimEpsilon/Stonks-Exchange/blob/master/img/vault.png)

L'interface se présente comme ceci :

![](https://github.com/TimEpsilon/Stonks-Exchange/blob/master/img/interface.png)

    - `player_head` : Affiche votre solde et votre solde max.
    - `nether_star` : Affiche le taux actuel.
    - `diamond` : Depose N diamants et M-Coins. Converti les diamants en leur équivalent en M-Coin.
    - `mcoin` : Retire N M-Coins
    - `wool` : Affiche votre rang et le prix du suivant.

Afin d'éviter toute fraude, chaque interaction est gardée en mémoire et consultable par l'administrateur dans `stonkexchange/logs`.

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

soit `n` un instant donné, définit comme un intervalle de `8h`. A l'instant `n+1`, le taux se décompose comme la somme de 5 termes : 

![Taux](https://github.com/TimEpsilon/Stonks-Exchange/blob/master/img/taux.PNG)

    - Le taux à l'instant `n`. Si les termes suivants sont nuls, le taux ne varie pas.
    - La pente, calculée à partir de 4 paramètres.
    - X, une valeur aléatoire comprise uniformément entre +/- 0.1
    - Les résidus, l'influence des pentes des instants précédents.
    - Le retour, la tendance du taux à revenir lentement vers sa valeur moyenne de 5.
    
##### Pente

4 paramètres dirigent cette pente. Ces paramètres, une fois leur valeur à l'instant `n`récupérée, sont injectés dans des fonctions `f(x)` qui renvoient une valeur entre +/- 1.

> La variation de la banque

<img src ="https://github.com/TimEpsilon/Stonks-Exchange/blob/master/img/variation_graph.png" width="250"> ![](https://github.com/TimEpsilon/Stonks-Exchange/blob/master/img/variation.PNG)

`m = -700` et `M = 3500` de sorte que `f(m) = -1/2` et `f(M) = 1/2`


> Le nombre de joueurs connectés

<img src ="https://github.com/TimEpsilon/Stonks-Exchange/blob/master/img/joueurs_graph.png" width="250"> ![](https://github.com/TimEpsilon/Stonks-Exchange/blob/master/img/joueurs.PNG)

`m = 5` et `a = 25` de sorte que `f(m) = 1/2`


> Le nombre de morts

<img src ="https://github.com/TimEpsilon/Stonks-Exchange/blob/master/img/mort_graph.png" width="250"> ![](https://github.com/TimEpsilon/Stonks-Exchange/blob/master/img/mort.PNG)

`m = 5` de sorte que `f(m) = -1/2`



> Le nombre de morts

<img src ="https://github.com/TimEpsilon/Stonks-Exchange/blob/master/img/diamonds_graph.png" width="250"> ![](https://github.com/TimEpsilon/Stonks-Exchange/blob/master/img/diamonds.PNG)

`m = 30` de sorte que `f(m) = -1/2`


Ces coefficients ne sont pas définitifs et peuvent être sujet à changement.


Une fois chacun des paramètres normalisés, ils sont sommés selon certains coefficients.

![](https://github.com/TimEpsilon/Stonks-Exchange/blob/master/img/somme.PNG)

`a = 0.4`, `b = 0.5`, `c = 0.7`, `d = 0.3`

Enfin, la pente est calculée en injectant la somme dans une dernière fonction.

![](https://github.com/TimEpsilon/Stonks-Exchange/blob/master/img/pente.PNG)

`a = 0.8`, `r = 5`

##### X

X suit une loi aléatoire à densité uniforme sur `[-0.1 , 0.1]`. Cette part d'aléatoire est également sujet à modification si besoin.

![](https://github.com/TimEpsilon/Stonks-Exchange/blob/master/img/unif.PNG)

##### Résidus

Les résidus sont une somme pondérée des pentes des 10 instants précédents. Leur influence décroit en `1/t`, il est donc possible de cumuler des instants à pente positive/négative pour faire exploser le taux.

![](https://github.com/TimEpsilon/Stonks-Exchange/blob/master/img/residus.PNG)

##### Retour

Le retour est la tendance du taux à converger vers sa valeur moyenne. Cette convergence est symétrique, augmente avec la distance à `taux = 5`, mais trop faible pour voir immédiatement son effet.

![](https://github.com/TimEpsilon/Stonks-Exchange/blob/master/img/retour.PNG)


### [S.A.M.]


**[S.A.M.]** est un outil mid-game qui permet de visualiser la valeur actuelle de ces paramètres ainsi que les 10 dernières valeurs du taux. C'est l'outil idéal pour tenter de prévoir quand le taux du M-Coin atteindra un maximum local.

<img src ="https://github.com/TimEpsilon/Stonks-Exchange/blob/master/img/graph.png" width="273"> ![](https://github.com/TimEpsilon/Stonks-Exchange/blob/master/img/craft.png)


## *Le Rang*

Pour éviter tout conflit quant à qui possède le plus d'argent, l'ancien affichage du solde total de chaque joueur n'est à présent plus possible. Un système de rangs a été introduit à sa place. Un rang est définit par son solde maximal et son prix d'achat et est symbolisé par une couleur. Lorsqu'un joueur atteint le plafond de son compte, il/elle peut acheter le rang suivant, changeant son statut social et la hauteur de son plafond.

Un total de 6 rangs existent :
    - Green, max = `25 M-Coins`
    - Blue, max = `100 M-Coins`
    - Purple, max = `500 M-Coins`
    - Red, max = `1 000 M-Coins`
    - Yellow, max = `5 000 M-Coins`
    - Gold, max = `20 000 M-Coins`

Mais d'autres rangs pourront être ajoutés à l'avenir.

Le rang de tous les joueurs est visible avec la commande `/baltop`
