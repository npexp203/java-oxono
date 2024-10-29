package g63549.dev.meteo.controller;

import g63549.dev.meteo.model.*;
import g63549.dev.meteo.view.MainView;

import java.time.LocalDate;

public class Controller {

    private final Model model;
    private final MainView view;

    public Controller(gmodel.Model model, MainView view) {
        this.model = model;
        this.view = view;
        this.view.setController(this); // Lien avec la vue pour les actions
    }

    /**
     * Action déclenchée lorsque l'utilisateur demande les données météo.
     *
     * @param address L'adresse saisie par l'utilisateur.
     * @param date    La date pour laquelle l'utilisateur souhaite obtenir la météo.
     */
    public void actionFetch(String address, LocalDate date) {
        try {
            // Récupération des données via le modèle
            WeatherObject weatherData = model.fetchWeather(address, date);

            // Mise à jour de la vue avec les données reçues
            view.update(weatherData);

        } catch (Exception e) {
            // Gestion des erreurs en affichant un message d'erreur dans la vue
            view.showError("Failed to fetch weather data: " + e.getMessage());
        }
    }
}
