package itmo;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.itmo.controller.IController;
import ru.itmo.controller.impl.ConsoleController;
import ru.itmo.dao.ICatDao;
import ru.itmo.dao.IOwnerDao;
import ru.itmo.dao.impl.CatDao;
import ru.itmo.dao.impl.OwnerDao;
import ru.itmo.dto.parser.ICatParser;
import ru.itmo.dto.parser.IOwnerParser;
import ru.itmo.dto.parser.impl.CatParser;
import ru.itmo.dto.parser.impl.OwnerParser;
import ru.itmo.service.ICatService;
import ru.itmo.service.IOwnerService;
import ru.itmo.service.impl.CatService;
import ru.itmo.service.impl.OwnerService;

public class Main {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory()) {
            IController controller = initConsoleController(sessionFactory);
            controller.start();
        }
    }

    private static IController initConsoleController(SessionFactory sessionFactory) {
        ICatParser catParser = new CatParser();
        IOwnerParser ownerParser = new OwnerParser();
        ICatDao catDao = new CatDao(sessionFactory);
        IOwnerDao ownerDao = new OwnerDao(sessionFactory);
        ICatService catService = new CatService(catDao, catParser);
        IOwnerService ownerService = new OwnerService(ownerDao, catDao, ownerParser);
        return new ConsoleController(catService, ownerService);
    }
}