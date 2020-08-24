/**
 * Solved by Pangsui
 */
public class Challenge9 {
    public static void main(String[] args) {
        final NewTutor tutor = new NewTutor();
        final NewStudent student = new NewStudent(tutor);
        tutor.setStudent(student);

        Thread tutorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                tutor.studyTime();
            }
        });

        Thread studentThread = new Thread(new Runnable() {
            @Override
            public void run() {
                student.handInAssignment();
            }
        });

        tutorThread.start();
        studentThread.start();
    }
}

class NewTutor {
    private NewStudent student;

    public void setStudent(NewStudent student) {
        synchronized (student){
            this.student = student;
        }
    }

    public void studyTime() {

            System.out.println("Tutor has arrived");
                try {
                    // wait for student to arrive
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
                synchronized (student) {
                    student.startStudy();
                }
                System.out.println("Tutor is studying with student");
    }

    public void getProgressReport() {
        // get progress report
        System.out.println("Tutor gave progress report");
    }
}

class NewStudent {

    private NewTutor tutor;

    NewStudent(NewTutor tutor) {
        synchronized (tutor){
            this.tutor = tutor;
        }
    }

    public void startStudy() {
        // study
        System.out.println("Student is studying");
    }

    public void handInAssignment() {
            tutor.getProgressReport();
                System.out.println("Student handed in assignment");
                synchronized (tutor){
                    tutor.notifyAll();
                }
    }
}