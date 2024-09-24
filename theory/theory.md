# TD  OO
## Question 1

### Exo 1 : 
Le code crée un point x, l'affiche, le déplace, puis l'affiche à nouveau donc en détail : le code crée le point en (0,0), ensuite il l'affiche puis le déplace en (2,2) puis le réaffiche

### Exo 2 :
Aucune erreur, car il est bien nommé en .java

### Exo 3 : 
Il aura une visibilité par défaut donc seulement dans le package donc si ils sont dans le meme paquet aucun soucis sinon oui de type :
error: Point is not public in oo_basics; cannot be accessed from outside package
### Exo 4 : 
Fait

# Question 2

## Exo 1 : 
java: x has private access in oo_inheritance.Point
## Exo 2 :
Sachant qu'il y a déjà le constructeur move il y aura une surcharge et donc
pour l'appel il y aura le (int,int)
## Exo 3 :
'move(double, double)' is already defined in 'oo_inheritance.Point'
## Exo 4 :
Fait

# Question 3 

## Exo 1 : 
Expected 0 arguments but found 2
## Exo 2 :
error: constructor Point in class Point cannot be applied to given types;
## Exo 3 :
Lorsque je vais supprimer tous les constructeurs java va donner un constructeur par défaut qui va juste initialiser les objets donc pas d'erreur et il affiche : (0.0, 0.0)
## Exo 4 :
Fait
# Question 4 

## Exo 1 :
création du point p en (0,0),
création du c en point p et rayon 5, print de c,déplacement du centre du cercle c à 2, print de c, multiplie le rayon du cercle par 2, et enfin un print donc : Circle : [(0.0, 0.0), 5.0]
Circle : [(2.0, 5.0), 5.0]
Circle : [(2.0, 5.0), 10.0]
## Exo 2 :
Que une seule
# Question 5

## Exo 1 : 
Circle : [(0.0, 0.0), 5.0]
Circle : [(2.0, 5.0), 5.0]
Circle : [(2.0, 5.0), 10.0]
## Exo 2 : 
Point : une seule instance,
Circle : une seule instance,
Réf des variables p et p2 :
fait référence au meme point p instancié, ref de l'attribut de l'instance center : aussi le point p
## Exo 3 :
Circle : [(0.0, 0.0), 5.0]
Circle : [(0.0, 0.0), 5.0]
## Exo 4 :
Circle : [(0.0, 0.0), 5.0]
Circle : [(0.0, 0.0), 5.0]
Circle : [(0.0, 0.0), 5.0]
## Exo 5 :
Point : il y a mtn 2 intances de point : le p et une copie de p , circle : ne change pas, les instances référencées par p et p2 : p refere la premiere instance de point et p2 refere mtn a une nvl instance de point crée par getCenter, l'attribut center c fait réf une nvl instance point 

# Question 6 : 
## Exo 1 : 
ColoredPoint p = new ColoredPoint(2, 4, 0xFF0000FF);
## Exo 2 :
L'erreur de compilation viendra lors de l'appel de p.getColor() car point ne contient pas cette methode
## Exo 3 :
Non, car ça produira une erreur de compil
## Exo 4 : 
Non, car ça produira une erreur de compil
## Exo 5 :
cyclic inheritance involving Point
## Exo 6 : 
cannot inherit from final Point
## Exo 7 : 
Fait

# Question 7 : 
## Exo 1 : 
Oui, car tout peut etre assigné à un object
## Exo 2 :
Oui, car tout peut etre assigné à un object
## Exo 3 :
Oui, car hashcode est une méthode de object, et donc hérite tout de object
## Exo 4 : 
Fait

# Question 8 :
## Exo 1 :
Call to 'super()' must be first statement in constructor body
## Exo 2 :
Constructor Point in class Point cannot be applied to given types

## Exo 3 :
non 
## Exo 4 :
Fait 

# Question 9 
## Exo 1 :
Classe c hérite de B et B hérite de A
donc le constructeur de la classe a est appelé en 1er ensuite le constructeur de la classe b et enfin le constructeur de C : constructor of A
constructor of B
constructor of C

## Exo 2 : 
le programme affichera pareil car y'aura le constructeur par défaut : 
constructor of A
constructor of B

## Exo 3 : 
l'affichage est tjrs le même

## Exo 4 : 
il y a Object() ça sera le plus vouent utilisé

# Question 10
## Exo 1 : 
(1.0, 1.0) - not pinned
(1.0, 1.0) - pinned
## Exo 2 :
Celle de PinnablePoint car l'objet référencé par la variable est de type PinnablePoint
## Exo 3 : 
L'erreur que j'obtiens est liée à l'utilisation de Exception, car Exception est une checked exception.
## Exo 4 :
Non, car IllegalStateException est une unchecked exception
## Exo 5 :
non car j'ai utilisé une unchecked exception comme illegalstateexception
## Exo 6 :
Non car pinnablepoint est une sous classe de point et on renvoie this
## Exo 7 :
non car object est une superclasse
## Exo 8 : 
Si jepublic par protected, je n'aurai pas d'erreur tant que la méthode est utilisée à l'intérieur du meme paquetage ou dans une sous-classe.
## Exo 9 : 
L'appel super.move(dx, dy) appelle la méthode move de la superclasse Point, qui permet de déplacer le point en modifiant ses coordonnées x et y.
## Exo 10 :
fait

# Question 11
## Exo 1 : 
 dans une interface, toutes les méthodes sont public et abstraites donc : modifier protected not allowed here
## Exo 2 :
le code aura pas d'erreur car c'est public
## Exo 3 :
## Exo 4 : 
Lorsque je remplace Point par Movable, le programme bénéficie du polymorphisme.
l'affichage sera : (3.0, 4.0)
 


# TD REGEX

## Question 1 

### Exo 1 : 
g\d{5}

### Exo 2 : 
g\d{5}\s+g\d{5}*

### Exo 3 :
\
^Bonjour.merci\\.$

### Exo 4 :

add\s+circle\s+\d+\s+\d+\s+\d+\s+\w

### Exo 5 : 

move\s+\d+\s+-?\d+\s+-?\d

## Question 2

Il contient XX-XX-XXXX
où x est un nombre de 0 à 9
donc le groupe zéro contient la correspondance complète de la chaine avec le motif