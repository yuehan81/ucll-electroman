package be.ucll.electroman.bootstrap;


import be.ucll.electroman.database.ElectromanDatabase;
import be.ucll.electroman.models.User;
import be.ucll.electroman.models.WorkOrder;
import be.ucll.electroman.repository.UserRepository;
import be.ucll.electroman.repository.WorkOrderRepository;

public class Bootstrap {


    private static long userId;


    public static void prepareDatabase(UserRepository userRepository, WorkOrderRepository workOrderRepository) {

        long userId;

        // create 1 User
        User user = new User();
        user.setFirstName("Johan");
        user.setLastName("Sermon");
        user.setMunicipality("Leuven");
        user.setPostalCode("3000");
        user.setStreet("Hondzochtstraat");
        user.setHouseNumber("48");
        user.setUsername("js");
        user.setPassword("js");
        userRepository.insert(user);


        userId = userRepository.findByUserName("js").getUid();
        // create 5 workorders
        WorkOrder workOrder1 = new WorkOrder("Brussels", "Dishwasher",
                "28H1", "Jean-Claude Vandamme", false,
                "The dishwasher started to make a lot of noise the last time " +
                        "we used it. Now that we try to use it again it seems there is no water being " +
                        "pumped into the dishwasher.", userId);
        WorkOrder workOrder2 = new WorkOrder("Charleroi", "Oven",
                "20F3", "Lucky Luck", false,
                "The timer of the oven does not work anymore.", userId);
        workOrderRepository.insertAll(workOrder1, workOrder2);
    }

    public static User populateUser() {

        // create 1 User
        User user = new User();
        user.setFirstName("Johan");
        user.setLastName("Sermon");
        user.setMunicipality("Leuven");
        user.setPostalCode("3000");
        user.setStreet("Hondzochtstraat");
        user.setHouseNumber("48");
        user.setUsername("js");
        user.setPassword("js");

        return user;
    }

    public static WorkOrder[] populateWorkOrders() {
        userId = 1;
        // create 5 workorders
        return new WorkOrder[] {
            new WorkOrder("Brussels", "Dishwasher",
                    "28H1", "Jean-Claude Vandamme", false,
                    "The dishwasher started to make a lot of noise the last time " +
                            "we used it. Now that we try to use it again it seems there is no water being " +
                            "pumped into the dishwasher.", userId),
            new WorkOrder("Charleroi", "Oven",
                    "20F3", "Lucky Luck", false,
                    "The timer of the oven does not work anymore.", userId)
        };

    }


}
