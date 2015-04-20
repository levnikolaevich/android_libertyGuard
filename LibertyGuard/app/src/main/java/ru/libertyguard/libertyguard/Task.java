package ru.libertyguard.libertyguard;

/**
 * Created by Лев on 14.04.2015.
 */
public class Task {

    int id;
    String name;
    String description;
    String TimeStart;
    String TimeEnd;
    int exp;
    int moderationStatusId;
    String moderationStatusName;
    int isTaskActive;
    int NeedFoto;
    int NeedData;

    String statusComplete;

    Task(int _id, String _name, String _description,  String _TimeStart, String _TimeEnd, int _exp,  int _moderationStatusId, String _moderationStatusName, int _isTaskActive,  int _NeedFoto, int _NeedData) {
        id = _id;
        name = _name;
        description = _description;
        TimeStart = _TimeStart;
        TimeEnd = _TimeEnd;
        exp = _exp;
        moderationStatusId = _moderationStatusId;
        moderationStatusName = _moderationStatusName;
        isTaskActive = _isTaskActive;
        NeedFoto = _NeedFoto;
        NeedData = _NeedData;
    }

}
