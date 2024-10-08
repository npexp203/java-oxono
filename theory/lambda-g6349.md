# Question 1 
## 1.
 List<Person> filteredList = filter(myList, p -> p.getLastname().startsWith("J"));
## 2.
List<Person> filteredList = filter(myList, p -> p.getLastname().startsWith("J") && p.getAge

# Question 2 
## 1. 
Collections.sort(list, (w1, w2) -> w1.length() - w2.length());
## 2.
Collections.sort(list, (w1, w2) -> Character.compare(w1.charAt(0), w2.charAt(0)));
# Question 3 
## 1.
ça ne fonctionnera pas car il faut spécifier le type pour les deux paramtetres
## 2.
ça ne fonctionnera pas car il faudra spécifier le type pour tous les paramètres et pas qu'un
## 3.
Correct

## 4.
non car pas de paranthèse

## 5. 
non car il n y a pas de return alors qu'il y a des {}

## 6.
ici un return mais pas d'{}

## 7.

non car pas d'{} et pas de point virgule

## 8.
oui car le compilateur peut deviner le type
