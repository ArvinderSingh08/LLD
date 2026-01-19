package Repository;

import Model.Group;
import Model.User;
import Service.GroupService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplitwiseRepository {
    private final Map<String, Group> store = new HashMap<>();
    GroupService groupService;

    public SplitwiseRepository(GroupService groupService) {
        this.groupService = groupService;
    }

    public Group createGroup(String name, List<User> users) {
        Group group = groupService.createGroup(name, users);
        store.put(group.getId(), group);
        return group;
    }

    public Group findGroupById(int id) {
        if(!store.containsKey(id)) {
            System.out.println("Group doesn't exist");
            return null;
        }
        return store.get(id);
    }
}
