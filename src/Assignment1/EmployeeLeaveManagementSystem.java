package Assignment1;
import java.util.*;

enum LeaveType {
    CASUAL, SICK, EARNED
}

enum LeaveStatus {
    PENDING, APPROVED, REJECTED
}

class EmployeeNotFoundException extends Exception {
    public EmployeeNotFoundException(String msg) { super(msg); }
}
class DuplicateEmployeeException extends Exception {
    public DuplicateEmployeeException(String msg) { super(msg); }
}
class InsufficientLeaveBalanceException extends Exception {
    public InsufficientLeaveBalanceException(String msg) { super(msg); }
}
class InvalidLeaveDaysException extends Exception {
    public InvalidLeaveDaysException(String msg) { super(msg); }
}
class LeaveRequestNotFoundException extends Exception {
    public LeaveRequestNotFoundException(String msg) { super(msg); }
}

abstract class Employee {
    private String employeeId;
    private String name;
    private String department;
    private String email;
    private int leaveBalance;

    // YEHI CONSTRUCTOR HAI - 5 parameters
    public Employee(String employeeId, String name, String department, String email, int leaveBalance) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.email = email;
        this.leaveBalance = leaveBalance;
    }

    public String getEmployeeId() { return employeeId; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public String getEmail() { return email; }
    public int getLeaveBalance() { return leaveBalance; }
    public void setLeaveBalance(int leaveBalance) { this.leaveBalance = leaveBalance; }

    public abstract void displayDetails();
}

class PermanentEmployee extends Employee {
    // SUPER ME SAME 5 PARAMETERS PASS HO RAHE HAIN
    public PermanentEmployee(String employeeId, String name, String department, String email, int leaveBalance) {
        super(employeeId, name, department, email, leaveBalance);
    }

    @Override
    public void displayDetails() {
        System.out.println("ID: " + getEmployeeId() + ", Name: " + getName() +
                ", Dept: " + getDepartment() + ", Email: " + getEmail() +
                ", Balance: " + getLeaveBalance() + ", Type: Permanent");
    }
}

class ContractEmployee extends Employee {
    // SUPER ME SAME 5 PARAMETERS PASS HO RAHE HAIN
    public ContractEmployee(String employeeId, String name, String department, String email, int leaveBalance) {
        super(employeeId, name, department, email, leaveBalance);
    }

    @Override
    public void displayDetails() {
        System.out.println("ID: " + getEmployeeId() + ", Name: " + getName() +
                ", Dept: " + getDepartment() + ", Email: " + getEmail() +
                ", Balance: " + getLeaveBalance() + ", Type: Contract");
    }
}

class LeaveRequest {
    private String requestId;
    private String employeeId;
    private LeaveType leaveType;
    private int numberOfDays;
    private String reason;
    private LeaveStatus status;

    public LeaveRequest(String requestId, String employeeId, LeaveType leaveType, int numberOfDays, String reason) {
        this.requestId = requestId;
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.numberOfDays = numberOfDays;
        this.reason = reason;
        this.status = LeaveStatus.PENDING;
    }

    public String getRequestId() { return requestId; }
    public String getEmployeeId() { return employeeId; }
    public LeaveType getLeaveType() { return leaveType; }
    public int getNumberOfDays() { return numberOfDays; }
    public String getReason() { return reason; }
    public LeaveStatus getStatus() { return status; }
    public void setStatus(LeaveStatus status) { this.status = status; }
}

public class EmployeeLeaveManagementSystem {
    private static HashMap<String, Employee> employeeMap = new HashMap<>();
    private static ArrayList<LeaveRequest> leaveRequests = new ArrayList<>();
    private static HashSet<String> emailSet = new HashSet<>();
    private static int leaveCounter = 1001;
    private static Scanner sc = new Scanner(System.in);

    /**
     * @param args
     */
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== Employee Leave Management System =====");
            System.out.println("1. Add Employee");
            System.out.println("2. Display All Employees");
            System.out.println("3. Search Employee");
            System.out.println("4. Apply Leave");
            System.out.println("5. Approve Leave");
            System.out.println("6. Reject Leave");
            System.out.println("7. Display All Leave Requests");
            System.out.println("8. Display Employee Leave Balance");
            System.out.println("9. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1: addEmployee(); break;
                    case 2: displayAllEmployees(); break;
                    case 3: searchEmployee(); break;
                    case 4: applyLeave(); break;
                    case 5: approveLeave(); break;
                    case 6: rejectLeave(); break;
                    case 7: displayAllLeaveRequests(); break;
                    case 8: displayEmployeeLeaveBalance(); break;
                    case 9: System.out.println("Exiting..."); return;
                    default: System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    static void addEmployee() throws DuplicateEmployeeException {
        System.out.print("Enter Employee ID: ");
        String id = sc.nextLine();
        System.out.print("Enter Employee Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Department: ");
        String dept = sc.nextLine();
        System.out.print("Enter Email: ");
        String email = sc.nextLine();
        System.out.print("Enter Leave Balance: ");
        int balance = sc.nextInt();
        sc.nextLine();
        System.out.print("Employee Type: Permanent/Contract: ");
        String type = sc.nextLine();

        if (employeeMap.containsKey(id) || emailSet.contains(email)) {
            throw new DuplicateEmployeeException("Duplicate Employee ID or Email not allowed.");
        }

        Employee emp;
        if (type.equalsIgnoreCase("Permanent")) {
            emp = new PermanentEmployee(id, name, dept, email, balance);
        } else if (type.equalsIgnoreCase("Contract")) {
            emp = new ContractEmployee(id, name, dept, email, balance);
        } else {
            System.out.println("Invalid type! Use Permanent/Contract");
            return;
        }

        employeeMap.put(id, emp);
        emailSet.add(email);
        System.out.println("Employee added successfully.");
    }

    static void displayAllEmployees() {
        if (employeeMap.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        Iterator<Employee> it = employeeMap.values().iterator();
        while (it.hasNext()) {
            it.next().displayDetails();
        }
    }

    static void searchEmployee() throws EmployeeNotFoundException {
        System.out.print("Enter Employee ID: ");
        String id = sc.nextLine();
        Employee emp = employeeMap.get(id);
        if (emp == null) throw new EmployeeNotFoundException("Employee " + id + " does not exist.");
        emp.displayDetails();
    }

    static void applyLeave() throws EmployeeNotFoundException, InvalidLeaveDaysException, InsufficientLeaveBalanceException {
        System.out.print("Enter Employee ID: ");
        String id = sc.nextLine();
        Employee emp = employeeMap.get(id);
        if (emp == null) throw new EmployeeNotFoundException("Employee " + id + " does not exist.");

        System.out.print("Enter Leave Type (CASUAL/SICK/EARNED): ");
        String typeStr = sc.nextLine().toUpperCase();
        LeaveType type;
        try {
            type = LeaveType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Leave Type!");
            return;
        }

        System.out.print("Enter Number of Days: ");
        int days = sc.nextInt();
        sc.nextLine();
        if (days <= 0) throw new InvalidLeaveDaysException("Leave days must be positive.");
        if (days > emp.getLeaveBalance()) throw new InsufficientLeaveBalanceException("Insufficient leave balance.");

        System.out.print("Enter Reason: ");
        String reason = sc.nextLine();

        String reqId = "LR" + (leaveCounter++);
        LeaveRequest req = new LeaveRequest(reqId, id, type, days, reason);
        leaveRequests.add(req);
        System.out.println("Leave request created successfully.");
        System.out.println("Request ID: " + reqId);
        System.out.println("Status: PENDING");
    }

    static void approveLeave() throws LeaveRequestNotFoundException, EmployeeNotFoundException {
        System.out.print("Enter Request ID: ");
        String reqId = sc.nextLine();
        LeaveRequest req = findRequest(reqId);
        if (req == null || req.getStatus() != LeaveStatus.PENDING)
            throw new LeaveRequestNotFoundException("Request not found or already processed.");

        Employee emp = employeeMap.get(req.getEmployeeId());
        if (emp == null) throw new EmployeeNotFoundException("Employee not found.");

        emp.setLeaveBalance(emp.getLeaveBalance() - req.getNumberOfDays());
        req.setStatus(LeaveStatus.APPROVED);
        System.out.println("Leave Request Approved. Balance updated.");
    }

    static void rejectLeave() throws LeaveRequestNotFoundException {
        System.out.print("Enter Request ID: ");
        String reqId = sc.nextLine();
        LeaveRequest req = findRequest(reqId);
        if (req == null || req.getStatus() != LeaveStatus.PENDING)
            throw new LeaveRequestNotFoundException("Request not found or already processed.");

        req.setStatus(LeaveStatus.REJECTED);
        System.out.println("Leave Request Rejected. Balance unchanged.");
    }

    static LeaveRequest findRequest(String reqId) {
        for (LeaveRequest r : leaveRequests) {
            if (r.getRequestId().equals(reqId)) return r;
        }
        return null;
    }

    static void displayAllLeaveRequests() {
        if (leaveRequests.isEmpty()) {
            System.out.println("No leave requests.");
            return;
        }
        for (LeaveRequest r : leaveRequests) {
            System.out.println("ID: " + r.getRequestId() + ", EmpID: " + r.getEmployeeId() +
                    ", Type: " + r.getLeaveType() + ", Days: " + r.getNumberOfDays() +
                    ", Reason: " + r.getReason() + ", Status: " + r.getStatus());
        }
    }

    static void displayEmployeeLeaveBalance() throws EmployeeNotFoundException {
        System.out.print("Enter Employee ID: ");
        String id = sc.nextLine();
        Employee emp = employeeMap.get(id);
        if (emp == null) throw new EmployeeNotFoundException("Employee " + id + " does not exist.");
        System.out.println("Leave Balance for " + id + " (" + emp.getName() + "): " + emp.getLeaveBalance());
    }
}