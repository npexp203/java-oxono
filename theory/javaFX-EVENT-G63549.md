# Question 1

## 1
lorsqu'on va executer la classe, et appuyer sur le btn insert, un texte est ajouté à la zone TextArea et des msg apparaissent dans la console.

# Question 2

## 1 
ce filtre va consommer tous les évents KEY_TYPED, donc tout ce qui sera saisi dans le text field ne sera pas affiché dans le text area, la méthode consume() empeche l'event de passer au node suivant de la chaine 

## 2 
tfdCharacter.addEventFilter(
    KeyEvent.KEY_TYPED,
    e -> {
        if (!e.getCharacter().matches("\\d")) {
            e.consume();
        }
    }
);
c'est une expression régulière pour vérifier si le caractère saisi est un chiffre (\\d). Si ce n'est pas le cas, l'évent est consommé, empêchant ainsi l'affichage des caracteres, mais autorisant l'affichage des chiffres.






