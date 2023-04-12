package be.ucll.electroman.bootstrap;


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

    public static User[] populateUsers() {

        // create 1st demo user
        User user1 = new User();
        user1.setFirstName("Johan");
        user1.setLastName("Sermon");
        user1.setMunicipality("Leuven");
        user1.setPostalCode("3000");
        user1.setStreet("Hondzochtstraat");
        user1.setHouseNumber("48");
        user1.setUsername("js");
        user1.setPassword("js");

        // create 1st demo user
        User user2 = new User();
        user2.setFirstName("Koen");
        user2.setLastName("Serneels");
        user2.setMunicipality("Brussel");
        user2.setPostalCode("1020");
        user2.setStreet("Boerzoektvrouwstraat");
        user2.setHouseNumber("79");
        user2.setUsername("ks");
        user2.setPassword("ks");

        User[] users = new User[]{user1, user2};

        return users;


    }

    public static WorkOrder[] populateWorkOrdersUser1() {
        userId = 1;
        // create 5 workorders
        WorkOrder[] workOrders = new WorkOrder[]{
                new WorkOrder("Brussels", "Dishwasher",
                        "28H1", "Jean-Claude Vandamme", false,
                        "The dishwasher started to make a lot of noise the last time " +
                                "we used it. Now that we try to use it again it seems there is no water being " +
                                "pumped into the dishwasher.", userId),
                new WorkOrder("Charleroi", "Oven",
                        "20F3", "Lucky Luke", false,
                        "The timer of the oven does not work anymore.", userId),
                new WorkOrder("Sint-Truiden", "Smart Speaker",
                        "20F3", "Chris Evans", false,
                        "The volume is very low and cannot be increased anymore to the original maximum level.", userId),
                new WorkOrder("Oostende", "Flatscreen TV",
                        "25C8", "Robert De Niro", false,
                        "The TV always turns off by itself after 20 minutes.", userId),
                new WorkOrder("Gent", "Video Doorbell",
                        "21E5", "Jennifer Lawrence", false,
                        "The video recording when somebody rings the bell stopped working.", userId),
                new WorkOrder("Mons", "Nespresso coffee machine",
                        "33L7", "George Clooney", false,
                        "The selection LCD screen is not working anymore. So I don't know which type of coffee I am selecting.", userId)

        };


        return workOrders;

    }

    public static WorkOrder[] populateWorkOrdersUser2() {
        userId = 2;
        // create 2 workorders
        WorkOrder[] workOrders = new WorkOrder[]{
                new WorkOrder("Antwerpen", "Electric Razor",
                        "45C6", "Denzel Washington", false,
                        "1 of the razor blades is not sharp anymore and should be replaced.", userId),
                new WorkOrder("Sint-Niklaas", "Vacuum cleaner",
                        "57K9", "Scarlett Johansson", false,
                        "The vacuum cleaner shuts down after 1 minute.", userId)

        };

        return workOrders;

    }


}
