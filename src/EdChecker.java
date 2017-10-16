import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import java.awt.Toolkit;
import java.util.Calendar;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;  


public class EdChecker {

    public static void main(String[] args) throws InterruptedException {
    	WebDriver driver ;
    	String userName = args[0];
    	String passWord = args[1];
    	Toolkit tk = Toolkit.getDefaultToolkit();
    	
    	System.setProperty("webdriver.gecko.driver", "C:\\Program Files\\Mozilla Firefox\\geckodriver.exe");
    	System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true"); 
    	System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
        driver =new FirefoxDriver();
        
        // Configure implicit Wait timeout for 20 seconds
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        // Go to web page
        String url ="http://www.thaiticketmajor.com/booking/1/zones.php?query=991";
        driver.get(url);
        // input Username and password
        driver.findElement(By.id("username")).sendKeys(userName);
        driver.findElement(By.id("password")).sendKeys(passWord);
        //Slide bar
        WebElement slider = driver.findElement(By.xpath("//*[@id=\"login\"]/div/div[2]/table/tbody/tr[5]/td/div/div[1]/div"));
        Actions move = new Actions(driver);
        Action action = (Action) move.dragAndDropBy(slider, 225, 0).build();
        action.perform();       
        driver.findElement(By.id("register2")).submit();
        driver.findElement(By.id("rdagree_y")).click();

        driver.findElement(By.id("btn_verify")).click();
     int Count=0;
     int TicketFound=0;
     String currentURL=null;
     TimeUnit.MILLISECONDS.sleep(10000);
     while(true) {
    	 	
    	 currentURL = driver.getCurrentUrl();
    	 //System.out.println("URL="+url);
    	 //System.out.println("CURRENT URL="+currentURL);
    	 if (currentURL.equals(url)) {
    		 //System.out.println("PAGE LOAD COMPLETED");
    	 } else {
    		 System.out.println("PAGE NOT FOUND ... REDIRECT TO ED PAGE");
    		 driver.get(url);
    		 TimeUnit.MILLISECONDS.sleep(5000);
    		 continue;
    	 }
    	 Count++;
    	 try {
    	 driver.findElement(By.partialLinkText("Seats Available")).click();
    
    	 } catch (NoSuchElementException e) {
    		 System.out.println("NOT FOUND Seat Available Button it might be in wrong page");
    		 continue;
    	 }
        int tb_size=driver.findElements(By.xpath("//*[@id=\"avail_data\"]/div/div/table/tbody/tr")).size();
        String first_path="//*[@id=\"avail_data\"]/div/div/table/tbody/tr[";
        String end_path="]/td[2]";
        String end_path1="]/td[1]";
        //int Counter=0;
        for (int i=2; i<tb_size; i++){
        	String full_path=first_path+i+end_path;
        	String seat=first_path+i+end_path1;
        	String tb_data=driver.findElement(By.xpath(full_path)).getText();
        	String tb_seat=driver.findElement(By.xpath(seat)).getText();
        	if (tb_data.equals("0") | (tb_data.equals("Sold Out") | tb_data.equals("") )){
        		
        		//System.out.println(tb_data);
        		//Counter++;
        	} else {
        		System.out.println("FOUND TICKET WITH STATUS= "+tb_seat+" : "+tb_data);
        		JavaSendmail(tb_seat,tb_data);
        		System.out.println("YEAH TICKET AVAILABLE");
        		TicketFound++;
        		tk.beep();
        		TimeUnit.MILLISECONDS.sleep(500);
        		tk.beep();
        		TimeUnit.MILLISECONDS.sleep(500);
        		tk.beep();
        		TimeUnit.MINUTES.sleep(5);
        	}
        	//System.out.println("NUMBER OF ROW"+Counter);
        }
        TimeUnit.MILLISECONDS.sleep(2000);
        WebElement body = driver.findElement(By.tagName("body"));
        body.sendKeys(Keys.ESCAPE);
        Calendar cal = Calendar.getInstance();
        
        System.out.println(cal.getTime()+" ==> END OF ROUND " +Count+"  Found Ticket = "+TicketFound);
        TimeUnit.SECONDS.sleep(8);
        
      }
    }
    public static void JavaSendmail(String m1, String m2) {
        //public static void main(String args[]) {
            Scanner sc = new Scanner(System.in);
            String username = "tongemu1.zilla@gmail.com";
            String password = "tongdcstest";
            String to = "tongpanuwat@gmail.com";
            String subject = "ED TICKET AVAILABLE NOW!!!";
            String email_body = "LET \'s HURRY!!! "+m1 +" Available : " +m2+" Seats" ;
            EdChecker test = new EdChecker();
            test.doSendMail(username, password, to, subject, email_body);
            sc.close();

       // }
    }
	// sends mail
	public void doSendMail(final String username, final String password, String to, String subject, String email_body) {
	
	    Properties props = new Properties();
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.socketFactory.port", "465");
	    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.port", "587");
	
	    Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
	        @Override
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(username, password);
	        }
	    });
	    try {
	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(username));
	        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
	        message.setSubject(subject);
	        message.setText(email_body);
	        Transport.send(message);
	        System.out.println("TICKET AVAILABLE ... EMAIL sent");
	        //JOptionPane.showMessageDialog(null, "Message Sent!", "Sent", JOptionPane.INFORMATION_MESSAGE);
	    } catch (Exception e) {
	        System.out.println(e);
	        JOptionPane.showMessageDialog(null, e.toString());
	    }
	}
}
