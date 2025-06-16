package ceng.ceng351.cengfactorydb;

import ceng.ceng351.cengfactorydb.QueryResult.LowSalaryEmployees;
import ceng.ceng351.cengfactorydb.QueryResult.MostValueableProduct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CENGFACTORYDB implements ICENGFACTORYDB{

    static final String USERNAME = "e2522159";
    static final String PASSWORD = "sXD9A$vpp$JF";
    static final String DB_NAME = "db2522159";
    static final String HOST = "144.122.71.128";
    static final String HOST_PORT = "8080";
    static final String DB_URL = "jdbc:mysql://" + HOST + ":" + HOST_PORT + "/" + DB_NAME;

    static Connection connection;
    static Statement statement;

    /**
     * Place your initialization code inside if required.
     *
     * <p>
     * This function will be called before all other operations. If your implementation
     * need initialization , necessary operations should be done inside this function. For
     * example, you can set your connection to the database server inside this function.
     */
    public void initialize() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Should create the necessary tables when called.
     *
     * @return the number of tables that are created successfully.
     */
    public int createTables() {
        String[] queries = new String[6];
        int numberOfTablesCreated = 0;

        queries[0] = "CREATE TABLE IF NOT EXISTS Factory (\n\t"
            + "factoryId INT,\n\t"
            + "factoryName TEXT,\n\t"
            + "factoryType TEXT,\n\t"
            + "country TEXT,\n\t"
            + "PRIMARY KEY (factoryId)\n"
            + ")";

        queries[1] = "CREATE TABLE IF NOT EXISTS Employee (\n\t"
            + "employeeId INT,\n\t"
            + "employeeName TEXT,\n\t"
            + "department TEXT,\n\t"
            + "salary INT,\n\t"
            + "PRIMARY KEY (employeeId)\n"
            + ")";

        queries[2] = "CREATE TABLE IF NOT EXISTS Works_In (\n\t"
            + "factoryId INT,\n\t"
            + "employeeId INT,\n\t"
            + "startDate DATE,\n\t"
            + "PRIMARY KEY (factoryId, employeeId),\n\t"
            + "FOREIGN KEY (factoryId) REFERENCES Factory(factoryId)\n\t\t"
            + "ON DELETE CASCADE ON UPDATE CASCADE,\n\t"
            + "FOREIGN KEY (employeeId) REFERENCES Employee(employeeId)\n\t\t"
            + "ON DELETE CASCADE ON UPDATE CASCADE\n"
            + ")";

        queries[3] = "CREATE TABLE IF NOT EXISTS Product (\n\t"
            + "productId INT,\n\t"
            + "productName TEXT,\n\t"
            + "productType TEXT,\n\t"
            + "PRIMARY KEY (productId)\n"
            + ")";

        queries[4] = "CREATE TABLE IF NOT EXISTS Produce (\n\t"
            + "factoryId INT,\n\t"
            + "productId INT,\n\t"
            + "amount INT,\n\t"
            + "productionCost INT,\n\t"
            + "PRIMARY KEY (factoryId, productId),\n\t"
            + "FOREIGN KEY (factoryId) REFERENCES Factory(factoryId)\n\t\t"
            + "ON DELETE CASCADE ON UPDATE CASCADE,\n\t"
            + "FOREIGN KEY (productId) REFERENCES Product(productId)\n\t\t"
            + "ON DELETE CASCADE ON UPDATE CASCADE\n"
            + ")";

        queries[5] = "CREATE TABLE IF NOT EXISTS Shipment (\n\t"
            + "factoryId INT,\n\t"
            + "productId INT,\n\t"
            + "amount INT,\n\t"
            + "pricePerUnit INT,\n\t"
            + "PRIMARY KEY (factoryId, productId),\n\t"
            + "FOREIGN KEY (factoryId) REFERENCES Factory(factoryId)\n\t\t"
            + "ON DELETE CASCADE ON UPDATE CASCADE,\n\t"
            + "FOREIGN KEY (productId) REFERENCES Product(productId)\n\t\t"
            + "ON DELETE CASCADE ON UPDATE CASCADE\n"
            + ")";

        for (String query : queries) {
            try {
                statement.executeUpdate(query);
                numberOfTablesCreated++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return numberOfTablesCreated;
    }

    /**
     * Should drop the tables if exists when called.
     *
     * @return the number of tables are dropped successfully.
     */
    public int dropTables() {
        String[] queries = new String[6];
        int numberOfTablesDropped = 0;

        // child tables
        queries[0] = "DROP TABLE IF EXISTS Works_In";
        queries[1] = "DROP TABLE IF EXISTS Produce";
        queries[2] = "DROP TABLE IF EXISTS Shipment";

        // parent tables
        queries[3] = "DROP TABLE IF EXISTS Factory";
        queries[4] = "DROP TABLE IF EXISTS Employee";
        queries[5] = "DROP TABLE IF EXISTS Product";

        for (String query : queries) {
            try {
                statement.executeUpdate(query);
                numberOfTablesDropped++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return numberOfTablesDropped;
    }

    /**
     * Should insert an array of Factory into the database.
     *
     * @return Number of rows inserted successfully.
     */
    public int insertFactory(Factory[] factories) {
        int numberOfFactoriesInserted = 0;

        String query = "INSERT INTO Factory (factoryId, factoryName, factoryType, country) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (Factory factory : factories) {
                preparedStatement.setInt(1, factory.getFactoryId());
                preparedStatement.setString(2, factory.getFactoryName());
                preparedStatement.setString(3, factory.getFactoryType());
                preparedStatement.setString(4, factory.getCountry());

                try {
                    preparedStatement.executeUpdate();
                    numberOfFactoriesInserted++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return numberOfFactoriesInserted;
    }

    /**
     * Should insert an array of Employee into the database.
     *
     * @return Number of rows inserted successfully.
     */
    public int insertEmployee(Employee[] employees) {
        int numberOfEmployeesInserted = 0;

        String query = "INSERT INTO Employee (employeeId, employeeName, department, salary) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (Employee employee : employees) {
                preparedStatement.setInt(1, employee.getEmployeeId());
                preparedStatement.setString(2, employee.getEmployeeName());
                preparedStatement.setString(3, employee.getDepartment());
                preparedStatement.setInt(4, employee.getSalary());

                try {
                    preparedStatement.executeUpdate();
                    numberOfEmployeesInserted++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return numberOfEmployeesInserted;
    }

    /**
     * Should insert an array of WorksIn into the database.
     *
     * @return Number of rows inserted successfully.
     */
    public int insertWorksIn(WorksIn[] worksIns) {
        int numberOfWorksInInserted = 0;

        String query = "INSERT INTO Works_In (factoryId, employeeId, startDate) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (WorksIn worksIn : worksIns) {
                preparedStatement.setInt(1, worksIn.getFactoryId());
                preparedStatement.setInt(2, worksIn.getEmployeeId());
                preparedStatement.setString(3, worksIn.getStartDate());

                try {
                    preparedStatement.executeUpdate();
                    numberOfWorksInInserted++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return numberOfWorksInInserted;
    }

    /**
     * Should insert an array of Product into the database.
     *
     * @return Number of rows inserted successfully.
     */
    public int insertProduct(Product[] products) {
        int numberOfProductsInserted = 0;

        String query = "INSERT INTO Product (productId, productName, productType) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (Product product : products) {
                preparedStatement.setInt(1, product.getProductId());
                preparedStatement.setString(2, product.getProductName());
                preparedStatement.setString(3, product.getProductType());

                try {
                    preparedStatement.executeUpdate();
                    numberOfProductsInserted++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return numberOfProductsInserted;
    }


    /**
     * Should insert an array of Produce into the database.
     *
     * @return Number of rows inserted successfully.
     */
    public int insertProduce(Produce[] produces) {
        int numberOfProducesInserted = 0;

        String query = "INSERT INTO Produce (factoryId, productId, amount, productionCost) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (Produce produce : produces) {
                preparedStatement.setInt(1, produce.getFactoryId());
                preparedStatement.setInt(2, produce.getProductId());
                preparedStatement.setInt(3, produce.getAmount());
                preparedStatement.setInt(4, produce.getProductionCost());

                try {
                    preparedStatement.executeUpdate();
                    numberOfProducesInserted++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return numberOfProducesInserted;
    }


    /**
     * Should insert an array of Shipment into the database.
     *
     * @return Number of rows inserted successfully.
     */
    public int insertShipment(Shipment[] shipments) {
        int numberOfShipmentsInserted = 0;

        String query = "INSERT INTO Shipment (factoryId, productId, amount, pricePerUnit) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (Shipment shipment : shipments) {
                preparedStatement.setInt(1, shipment.getFactoryId());
                preparedStatement.setInt(2, shipment.getProductId());
                preparedStatement.setInt(3, shipment.getAmount());
                preparedStatement.setInt(4, shipment.getPricePerUnit());

                try {
                    preparedStatement.executeUpdate();
                    numberOfShipmentsInserted++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return numberOfShipmentsInserted;
    }

    /**
     * Should return all factories that are located in a particular country.
     *
     * @return Factory[]
     */
    public Factory[] getFactoriesForGivenCountry(String given_country) {
        List<Factory> factories = new ArrayList<>();

        String query = "SELECT DISTINCT F.* FROM Factory F WHERE F.country = ? ORDER BY F.factoryId";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, given_country);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int factoryId = rs.getInt(1);
                String factoryName = rs.getString(2);
                String factoryType = rs.getString(3);
                String country = rs.getString(4);
                factories.add(new Factory(factoryId, factoryName, factoryType, country));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return factories.toArray(new Factory[0]);
    }



    /**
     * Should return all factories without any working employees.
     *
     * @return Factory[]
     */
    public Factory[] getFactoriesWithoutAnyEmployee() {
        List<Factory> factories = new ArrayList<>();

        String query = "SELECT DISTINCT F.* FROM Factory F\n"
            + "WHERE F.factoryId IN\n"
            + "(SELECT F.factoryId FROM Factory F\n"
            + "EXCEPT\n"
            + "SELECT WI.factoryId FROM Works_In WI)\n"
            + "ORDER BY F.factoryId";

        try {
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int factoryId = rs.getInt(1);
                String factoryName = rs.getString(2);
                String factoryType = rs.getString(3);
                String country = rs.getString(4);
                factories.add(new Factory(factoryId, factoryName, factoryType, country));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return factories.toArray(new Factory[0]);
    }

    /**
     * Should return all factories that produce all products for a particular productType
     *
     * @return Factory[]
     */
    public Factory[] getFactoriesProducingAllForGivenType(String productType) {
        List<Factory> factories = new ArrayList<>();

        String query = "SELECT DISTINCT F.* FROM Factory F\n"
            + "WHERE NOT EXISTS\n"
            + "(SELECT P.productId FROM Product P WHERE P.productType = ?\n"
            + "EXCEPT\n"
            + "SELECT P.productId FROM Produce P WHERE P.factoryId = F.factoryId)\n"
            + "ORDER BY F.factoryId";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, productType);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int factoryId = rs.getInt(1);
                String factoryName = rs.getString(2);
                String factoryType = rs.getString(3);
                String country = rs.getString(4);
                factories.add(new Factory(factoryId, factoryName, factoryType, country));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return factories.toArray(new Factory[0]);
    }


    /**
     * Should return the products that are produced in a particular factory but
     * don’t have any shipment from that factory.
     *
     * @return Product[]
     */
    public Product[] getProductsProducedNotShipped() {
        List<Product> products = new ArrayList<>();

        String query = "SELECT DISTINCT P.* FROM Product P, Factory F\n"
            + "WHERE P.productId IN\n"
            + "(SELECT productId FROM Produce PE WHERE PE.factoryId = F.factoryId\n"
            + "EXCEPT\n"
            + "SELECT PE.productId FROM Produce PE, Shipment S\n"
            + "WHERE PE.productId = S.productId\n"
            + "AND PE.factoryId = S.factoryId\n"
            + "AND PE.factoryId = F.factoryId)\n"
            + "ORDER BY P.productId";

        try {
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int productId = rs.getInt(1);
                String productName = rs.getString(2);
                String productType = rs.getString(3);
                products.add(new Product(productId, productName, productType));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products.toArray(new Product[0]);
    }


    /**
     * For a given factoryId and department, should return the average salary of
     *     the employees working in that factory and that specific department.
     *
     * @return double
     */
    public double getAverageSalaryForFactoryDepartment(int factoryId, String department) {
        double averageSalary = 0.0;

        String query = "SELECT AVG(E.salary) AS averageSalary\n" +
                        "FROM Employee E, Works_In WI\n" +
                        "WHERE E.employeeId = WI.employeeId\n" +
                        "AND WI.factoryId = ?\n" +
                        "AND E.department = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, factoryId);
            preparedStatement.setString(2, department);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                averageSalary = rs.getInt("averageSalary");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return averageSalary;
    }


    /**
     * Should return the most profitable products for each factory
     *
     * @return QueryResult.MostValueableProduct[]
     */
    public QueryResult.MostValueableProduct[] getMostValueableProducts() {

        List<MostValueableProduct> mostValuableProducts = new ArrayList<>();

        String query = "SELECT DISTINCT Result.factoryId, P.*, Result.maxProfit\n" +
                       "FROM Product P,\n" +
                       "(SELECT Temp.factoryId, Temp.productId, Temp.profit AS maxProfit\n" +
                       "FROM\n" +
                       "        (SELECT PE.productId, PE.factoryId, (IF(S.amount IS NULL, 0, S.amount) * IF(S.pricePerUnit IS NULL, 0, S.pricePerUnit) - PE.amount * PE.productionCost) AS profit\n"
                       +
                       "        FROM Produce PE\n" +
                       "        LEFT OUTER JOIN Shipment S\n" +
                       "        ON PE.factoryId = S.factoryId AND PE.productId = S.productId\n" +
                       "        ) AS Temp,\n" +
                       "        (SELECT PE.factoryId, MAX(IF(S.amount IS NULL, 0, S.amount) * IF(S.pricePerUnit IS NULL, 0, S.pricePerUnit) - PE.amount * PE.productionCost) AS maxProfit\n"
                       +
                       "        FROM Produce PE\n" +
                       "        LEFT OUTER JOIN Shipment S\n" +
                       "        ON PE.factoryId = S.factoryId AND PE.productId = S.productId\n" +
                       "\n" +
                       "        GROUP BY PE.factoryId\n" +
                       "        ) AS MaxTemp\n" +
                       "    WHERE Temp.profit = MaxTemp.maxProfit\n" +
                       "    AND Temp.factoryId = MaxTemp.factoryId\n" +
                       ") AS Result\n" +
                       "WHERE P.productId = Result.productId\n" +
                       "ORDER BY Result.maxProfit DESC, Result.factoryId\n";


        try {
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int factoryId = rs.getInt(1);
                int productId = rs.getInt(2);
                String productName = rs.getString(3);
                String productType = rs.getString(4);
                double profit = rs.getDouble(5);

                mostValuableProducts.add(new MostValueableProduct(factoryId, productId, productName, productType, profit));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return mostValuableProducts.toArray(new MostValueableProduct[0]);
    }

    /**
     * For each product, return the factories that gather the highest profit
     * for that product
     *
     * @return QueryResult.MostValueableProduct[]
     */
    public QueryResult.MostValueableProduct[] getMostValueableProductsOnFactory() {
        List<MostValueableProduct> mostValuableProducts = new ArrayList<>();

        String query = "SELECT DISTINCT Result.factoryId, P.*, Result.maxProfit\n" +
                       "FROM Product P,\n" +
                       "(SELECT Temp.factoryId, Temp.productId, Temp.profit AS maxProfit\n" +
                       "FROM\n" +
                       "(SELECT PE.productId, PE.factoryId, (IF(S.amount IS NULL, 0, S.amount) * IF(S.pricePerUnit IS NULL, 0, S.pricePerUnit) - PE.amount * PE.productionCost) AS profit\n"
                       +
                       "FROM Produce PE\n" +
                       "LEFT OUTER JOIN Shipment S\n" +
                       "ON PE.productId = S.productId AND PE.factoryId = S.factoryId\n" +
                       ") AS Temp,\n" +
                       "(SELECT PE.productId, MAX(IF(S.amount IS NULL, 0, S.amount) * IF(S.pricePerUnit IS NULL, 0, S.pricePerUnit) - PE.amount * PE.productionCost) AS maxProfit\n"
                       +
                       "FROM Produce PE\n" +
                       "LEFT OUTER JOIN Shipment S\n" +
                       "ON PE.productId = S.productId AND PE.factoryId = S.factoryId\n" +
                       "GROUP BY PE.productId\n" +
                       ") AS MaxTemp\n" +
                       "WHERE Temp.profit = MaxTemp.maxProfit\n" +
                       "AND Temp.productId = MaxTemp.productId\n" +
                       ") AS Result\n" +
                       "WHERE P.productId = Result.productId\n" +
                       "ORDER BY Result.maxProfit DESC, Result.productId\n";

        try {
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int factoryId = rs.getInt(1);
                int productId = rs.getInt(2);
                String productName = rs.getString(3);
                String productType = rs.getString(4);
                double profit = rs.getDouble(5);

                mostValuableProducts.add(new MostValueableProduct(factoryId, productId, productName, productType, profit));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return mostValuableProducts.toArray(new MostValueableProduct[0]);
    }


    /**
     * For each department, should return all employees that are paid under the
     *     average salary for that department. You consider the employees
     *     that do not work earning ”0”.
     *
     * @return QueryResult.LowSalaryEmployees[]
     */
    public QueryResult.LowSalaryEmployees[] getLowSalaryEmployeesForDepartments() {

        List<LowSalaryEmployees> lowSalaryEmployees = new ArrayList<>();

        String query = "SELECT E.employeeId AS employeeId, E.employeeName, E.department, E.salary FROM Employee E,\n" +
                       "    (SELECT X.department AS department, AVG(X.salary) AS averageSalary\n" +
                       "    FROM (\n" +
                       "            SELECT E3.department AS department, E3.salary AS salary\n" +
                       "            FROM Employee E3\n" +
                       "            WHERE E3.employeeId IN (\n" +
                       "            SELECT E2.employeeId\n" +
                       "            FROM Employee E2, Works_In WI\n" +
                       "            WHERE E2.employeeId = WI.employeeId\n" +
                       "            )\n" +
                       "                UNION ALL\n" +
                       "            SELECT E3.department AS department, 0 AS salary\n" +
                       "            FROM Employee E3\n" +
                       "            WHERE E3.employeeId NOT IN (\n" +
                       "            SELECT E2.employeeId\n" +
                       "            FROM Employee E2, Works_In WI\n" +
                       "            WHERE E2.employeeId = WI.employeeId\n" +
                       "            )\n" +
                       "        ) AS X\n" +
                       "        GROUP BY X.department\n" +
                       "    ) AS Temp\n" +
                       "WHERE E.department = Temp.department\n" +
                       "AND E.salary < Temp.averageSalary\n" +
                       "\n" +
                       "UNION\n" +
                       "\n" +
                       "SELECT E3.employeeId AS employeeId, E3.employeeName, E3.department, 0 AS salary\n" +
                       "    FROM Employee E3\n" +
                       "    WHERE E3.employeeId NOT IN (\n" +
                       "        SELECT E2.employeeId\n" +
                       "        FROM Employee E2, Works_In WI\n" +
                       "        WHERE E2.employeeId = WI.employeeId\n" +
                       "    )\n" +
                       "\n" +
                       "ORDER BY employeeId\n";

        try {
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int employeeId = rs.getInt(1);
                String employeeName = rs.getString(2);
                String department =  rs.getString(3);
                int salary = rs.getInt(4);

                lowSalaryEmployees.add(new LowSalaryEmployees(employeeId, employeeName, department, salary));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lowSalaryEmployees.toArray(new LowSalaryEmployees[0]);
    }


    /**
     * For the products of given productType, increase the productionCost of every unit by a given percentage.
     *
     * @return number of rows affected
     */
    public int increaseCost(String productType, double percentage) {
        int numberOfCostIncreased = 0;

        String query =
            "UPDATE Produce PE\n" +
            "SET PE.productionCost = PE.productionCost * (1 + ?)\n" +
            "WHERE PE.productId IN (\n" +
                "SELECT PT.productId\n" +
                "FROM Product PT, Produce PE\n" +
                "WHERE PT.productId = PE.productId AND\n" +
                "PT.productType = ?\n" +
            ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, percentage);
            preparedStatement.setString(2, productType);

            numberOfCostIncreased = preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return numberOfCostIncreased;
    }


    /**
     * Deleting all employees that have not started working since the given date.
     *
     * @return number of rows affected
     */
    public int deleteNotWorkingEmployees(String givenDate) {
        int numberOfEmployeesDeleted = 0;

        String query = "DELETE FROM Employee WHERE employeeId IN (\n" +
                "SELECT E2.employeeId FROM Employee E2\n" +
                "WHERE NOT EXISTS (\n" +
                    "SELECT * FROM Works_In WI\n" +
                    "WHERE E2.employeeId = WI.employeeId\n" +
                    "AND WI.startDate >= ?\n" +
                ")\n" +
            ")\n";
        /*
        * | * -> don't delete
        * | _ -> delete
        _ | * -> don't delete
        _ | _ -> delete
         */

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, givenDate);

            numberOfEmployeesDeleted = preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return numberOfEmployeesDeleted;
    }


    /**
     * *****************************************************
     * *****************************************************
     * *****************************************************
     * *****************************************************
     *  THE METHODS AFTER THIS LINE WILL NOT BE GRADED.
     *  YOU DON'T HAVE TO SOLVE THEM, LEAVE THEM AS IS IF YOU WANT.
     *  IF YOU HAVE ANY QUESTIONS, REACH ME VIA EMAIL.
     * *****************************************************
     * *****************************************************
     * *****************************************************
     * *****************************************************
     */

    /**
     * For each department, find the rank of the employees in terms of
     * salary. Peers are considered tied and receive the same rank. After
     * that, there should be a gap.
     *
     * @return QueryResult.EmployeeRank[]
     */
    public QueryResult.EmployeeRank[] calculateRank() {
        return new QueryResult.EmployeeRank[0];
    }

    /**
     * For each department, find the rank of the employees in terms of
     * salary. Everything is the same but after ties, there should be no
     * gap.
     *
     * @return QueryResult.EmployeeRank[]
     */
    public QueryResult.EmployeeRank[] calculateRank2() {
        return new QueryResult.EmployeeRank[0];
    }

    /**
     * For each factory, calculate the most profitable 4th product.
     *
     * @return QueryResult.FactoryProfit
     */
    public QueryResult.FactoryProfit calculateFourth() {
        return new QueryResult.FactoryProfit(0,0,0);
    }

    /**
     * Determine the salary variance between an employee and another
     * one who began working immediately after the first employee (by
     * startDate), for all employees.
     *
     * @return QueryResult.SalaryVariant[]
     */
    public QueryResult.SalaryVariant[] calculateVariance() {
        return new QueryResult.SalaryVariant[0];
    }

    /**
     * Create a method that is called once and whenever a Product starts
     * losing money, deletes it from Produce table
     *
     * @return void
     */
    public void deleteLosing() {

    }
}
