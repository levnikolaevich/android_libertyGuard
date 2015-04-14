package ru.libertyguard.libertyguard;

/**
 * Created by Лев on 12.04.2015.
 */
public class Quest {

    int id;
    String name;
    int exp;
    String startDate;
    int countTask;
    int idUEQ;
    int statusId;
    String statusComplete;

    Quest(int _id, String _name, int _exp, String _startDate, int _countTask, int _idUEQ, int _statusId, String _statusComplete) {
        id = _id;
        name = _name;
        exp = _exp;
        startDate = _startDate;
        countTask = _countTask;
        idUEQ = _idUEQ;
        statusId = _statusId;
        statusComplete = _statusComplete;
    }

}
