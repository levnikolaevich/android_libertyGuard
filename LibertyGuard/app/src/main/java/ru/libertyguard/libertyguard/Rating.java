package ru.libertyguard.libertyguard;

/**
 * Рейтинг 5/2/2015.
 */
public class Rating {

    int idUser;
    String name;
    int exp;

    Rating(int _idUser, String _name, int _exp) {
        idUser = _idUser;
        name = _name;
        exp = _exp;
    }
}