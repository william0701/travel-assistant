import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import org.apache.commons.io.IOUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HelloWeChat extends HttpServlet
{
	public static void main(String args[])
	{
		Scanner s=new Scanner(System.in);
		LetsChat(s.next());
	}
    private static final long serialVersionUID = 1L;
    public static String LetsChat(String s) {
    	//System.out.println("msg1");
		// TODO Auto-generated method stub
    		
    	String mctg="<xml><ToUserName><![CDATA[toUser]]></ToUserName><FromUserName><![CDATA[fromUser]]></FromUserName>  <CreateTime>1348831860</CreateTime> <MsgType><![CDATA[text]]></MsgType>  <Content><![CDATA["+s+"]]></Content> <MsgId>wen</MsgId> </xml>";
    		WeChatTextMessage msg=getWeChatTextMessage(mctg);
    		//System.out.println("msg1");
    		//System.out.println(msg.toString());
    		String[] arrs = new String[2];//定义一个字符串数组
    		split sp = new split((String) msg.getContent(), arrs);
    		//System.out.println(getReplyTextMessage(msg.content,msg.fromUserName));
    		String Use_info= new String();
    		String Info= new String();
    		String meg1=new String();
    		try{
    			switch( Integer.parseInt(arrs[0]))
        		{
        		case 1:
        			{
        				Use_info = arrs[1];
        				//System.out.println(checkForDigit(Use_info));
        				if(containsAny(Use_info,"#")&&checkForDigit(Use_info))
        					throw new Exception();
        				else 
        					Info= Use_info+'#'+(String) msg.getmsgId();
        					meg1 = new queryfunction().bindcity(Info);
        					System.out.println(meg1);
        					break;
        					
        			}
        		case 2:
        			{
        				Use_info = arrs[1];
        				//System.out.println(containsAny(Use_info,"#")||!checkForDigit(Use_info));
        				if(containsAny(Use_info,"#")||!checkForDigit(Use_info))
        					throw new Exception();
        				else 
        					Info= Use_info+'#'+(String) msg.getmsgId();
    						meg1 = new queryfunction().busquery(Info);
    						System.out.println(meg1);
        					break;
        			}
        		case 3:
        			{
        				Use_info = arrs[1];
        				//System.out.println(checkForDigit(Use_info));
        				if(containsAny(Use_info,"#")&&!checkForDigit(Use_info))
        				{	Info= Use_info+'#'+(String) msg.getmsgId();
    						meg1 = new queryfunction().linequery(Info);
    						System.out.println(meg1);
        					break;}
        				else 
        					throw new Exception();
        			}
        		 }
    		}catch(Exception e)
    		{
    			System.out.println("格式有误");
    		}
    		String rxml = getReplyTextMessage(meg1,msg.fromUserName);
    		System.out.println(rxml);
    		return rxml;
    	}

    static boolean containsAny(String str, String searchChars) {
            if(str.contains(searchChars))
                return true;
            else
                return false;
          }
    static boolean checkForDigit(String s)
    	{
    	  boolean  b=false;
    	  for(int i=0;i<s.length();i++)
    	  { if(Character.isDigit(s.charAt(i)))
    	     {b=true;  break;}
    	   }
    	    return  b;
    	}
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                // TODO 为了简单起见,先不对消息来源进行校验
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter pw = response.getWriter();
                String echo = request.getParameter("echostr");
                echo = new String(echo.getBytes("ISO-8859-1"),"UTF-8");
                pw.println(echo);
        }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter pw = response.getWriter();
                String wxMsgXml = IOUtils.toString(request.getInputStream(),"utf-8");
                WeChatTextMessage textMsg = null;
                try {
                        textMsg = getWeChatTextMessage(wxMsgXml);
                } catch (Exception e) {
                        e.printStackTrace();
                }
                StringBuffer replyMsg = new StringBuffer();
                if(textMsg != null){
                        //增加你所需要的处理逻辑，这里只是简单重复消息
                        replyMsg.append("您给我的消息是：");
                        replyMsg.append(textMsg.getContent());
                }
                else{
                        replyMsg.append(":)不是文本的消息，我暂时看不懂");
                }
                String returnXml = getReplyTextMessage(replyMsg.toString(), textMsg.getFromUserName());
                pw.println(returnXml);
        }
        
      static WeChatTextMessage getWeChatTextMessage(String xml){
                XStream xstream = new XStream(new DomDriver());
                xstream.alias("xml", WeChatTextMessage.class);
                xstream.aliasField("ToUserName", WeChatTextMessage.class, "toUserName");
                xstream.aliasField("FromUserName", WeChatTextMessage.class, "fromUserName");
                xstream.aliasField("CreateTime", WeChatTextMessage.class, "createTime");
                xstream.aliasField("MsgType", WeChatTextMessage.class, "messageType");
                xstream.aliasField("Content", WeChatTextMessage.class, "content");
                xstream.aliasField("MsgId", WeChatTextMessage.class, "msgId");
                WeChatTextMessage wechatTextMessage = (WeChatTextMessage)xstream.fromXML(xml); 
                return wechatTextMessage;
        }
        

        static String getReplyTextMessage(String content, String weChatUser){
            WeChatReplyTextMessage we = new WeChatReplyTextMessage();
            //we.setMessageType("text");
            //we.setFuncFlag("0");
            //we.setCreateTime(new Long(new Date().getTime()).toString());
            //we.setContent(content);
            //we.setToUserName(weChatUser);
            //we.setFromUserName("shanghaiweather");//TODO 你的公众帐号微信号
            we.set(content, weChatUser);
            XStream xstream = new XStream(new DomDriver()); 
            xstream.alias("xml", WeChatReplyTextMessage.class);
            xstream.aliasField("ToUserName", WeChatReplyTextMessage.class, "toUserName");
            xstream.aliasField("FromUserName", WeChatReplyTextMessage.class, "fromUserName");
            xstream.aliasField("CreateTime", WeChatReplyTextMessage.class, "createTime");
            xstream.aliasField("MsgType", WeChatReplyTextMessage.class, "messageType");
            xstream.aliasField("Content", WeChatReplyTextMessage.class, "content");
            xstream.aliasField("FuncFlag", WeChatReplyTextMessage.class, "funcFlag");
            String xml =xstream.toXML(we);
            return xml;
    }
}
class WeChatTextMessage {
	String toUserName;
	String fromUserName;
	String createTime;
	String messageType;//text
	String content;
	String msgId;
	 //getter and setter
    public String toString()
    {
       return(this.toUserName+"\n"+this.fromUserName+"\n"+this.createTime+"\n"+this.messageType+"\n"+this.content+"\n"+this.msgId);
    }
	
	Object getContent() {
		// TODO Auto-generated method stub
		return this.content;
	}
	String getFromUserName() {
		// TODO Auto-generated method stub
		return this.fromUserName;
	}

	String getmsgId() {
		// TODO Auto-generated method stub
		return this.msgId;
	}

}




class WeChatReplyTextMessage {

 public String toUserName;
 public String fromUserName;
 public String createTime;
 public String messageType;
 public String content;
 public String funcFlag;
 //getter and setter
public void setMessageType(String s) {
	// TODO Auto-generated method stub
	this.messageType=s;
}
public void setFuncFlag(String s) {
	// TODO Auto-generated method stub
	this.funcFlag=s;
}
public void setCreateTime(String s) {
	// TODO Auto-generated method stub
	this.createTime=s;
}
public void setContent(String s) {
	// TODO Auto-generated method stub
	this.content=s;
}
public void setToUserName(String s) {
	// TODO Auto-generated method stub
	this.toUserName=s;
}
public void setFromUserName(String s) {
	// TODO Auto-generated method stub
	this.fromUserName=s;
}
public void set(String content,String weChatUser) {
	// TODO Auto-generated method stub
	this.setMessageType("text");
	this.setFuncFlag("0");
    this.setCreateTime(new Long(new Date().getTime()).toString());
    this.setContent(content);
    this.setToUserName(weChatUser);
    this.setFromUserName("公交查询");
}
}
class queryfunction 
{
	static final String driver = "com.mysql.jdbc.Driver";
	static final String url = "jdbc:mysql://localhost:3306/busline";
	static final String User = "root";
	static final String password = "1";
	String sql;
	queryfunction()
	{
		try{
			Class.forName(driver);
			
			
		}
		catch(Exception e)
		{}
	}

	String bindcity(String paramter)
	{
		char p[]=paramter.toCharArray();
		int i=0;
		String city=new String(),user=new String();
		while(p[i]!='#')
		{
			city=city+p[i];
			i++;
		}
		i++;
		user=paramter.substring(i);
		String rst=null;
		ResultSet rs = null;
		try 
		{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, User, password);
			if(conn.isClosed()){System.out.println("Database connection error");}
			Statement statement=conn.createStatement();
			sql="select CITY from city where CITY='"+city+"';";
			System.out.println(sql);
			rs=statement.executeQuery(sql);
			if(!rs.next())
				return "抱歉：您所输入的城市未在数据库中";
			MTbusline.UPdate_user_city(user,city);
			return "恭喜!您的城市已绑定成功";
		}
		catch(Exception e)
		{}
		return null;
	}
	String busquery(String parameter)
	{
		String bus_no;
		String user;
		String city;
		ResultSet rst;
		String result=new String();
		try
		{
			Connection conn=DriverManager.getConnection(url, User, password);
			Statement statement=conn.createStatement();
			if(conn.isClosed()){System.out.println("Database connection error");}
			char p[]=parameter.toCharArray();
			int i=0;
			bus_no=new String();
			user=new String();
			while(p[i]!='#')
			{
				bus_no=bus_no+p[i];
				i++;
			}
			i++;
			user=parameter.substring(i);
			sql="select CITY from user_city where user='"+user+"';";
			rst=statement.executeQuery(sql);
			if(!rst.next()) return "抱歉，您所查询的城市未绑定。请输入\"1+城市名进行绑定\"进行城市“绑定";
			city=new String(rst.getString("CITY"));
			sql="select * from bus_time where CITY='"+city+"'and BUS_NO='"+bus_no+"';";
			rst=statement.executeQuery(sql);
			if(!rst.next()) return "抱歉。您所绑定的城市不存在名为\""+bus_no+"\"的公交线路。请核对后重新输入或更改绑定城市。";
			result="线路\t首班车时间\t\t末班车时间\t\t全程时间\n";
			result+="--------------------------------------------------\n";
			String bn=rst.getString("BUS_NO");
			String st=rst.getString("FIRST_TIME");
			String lt=rst.getString("LAST_TIME");
			String wt=rst.getString("WHOLE_TIME");
			String temp=bn+"\t"+st+"\t"+lt+"\tt"+wt+"\n";
			result=result+temp;
			result+="--------------------------------------------------\n";
			result+="线路\t站号\t站名\t\t距离\n";
			result+="--------------------------------------------------\n";
			sql="select* from bus_line where CITY='"+city+"'and BUS_NO='"+bus_no+"';";
			rst=statement.executeQuery(sql);
			temp="";
			while(rst.next())
		   	{
				bn=rst.getString("BUS_NO");
				st=rst.getString("STATION");//站名
		   		String rk=rst.getString("RANK");
		   		String dc=rst.getString("DISTANCE");
		   		temp=bn+"\t"+rk+"\t"+st+"\t\t"+dc;
		   		//System.out.println(temp);
		   		result=result+"\n"+temp;
		   	}
			return result;
		}
		catch(Exception e)
		{}
		return null;
	}
	String linequery(String parameter)
	{
		String start,destination;
		String user;
		String city;
		ResultSet rst;
		String result=new String();
		try
		{
			Connection conn=DriverManager.getConnection(url, User, password);
			Statement statement=conn.createStatement();
			if(conn.isClosed()){System.out.println("Database connection error");}
			start=parameter.split("#")[0];
			destination=parameter.split("#")[1];
			user=parameter.split("#")[2];
			sql="select CITY from user_city where user='"+user+"';";
			rst=statement.executeQuery(sql);
			if(!rst.next()) return "抱歉，您所查询的城市未绑定。请输入\"1+城市名进行绑定\"进行城市“绑定";
			city=new String(rst.getString("CITY"));
			result=new Transfer().busStationToStation(start,destination,city);
		}
		catch(Exception e)
		{}
		return result;
	}
}


class Transfer
{
	Connection conn;
	Statement stm;
	ResultSet rs;
	String conUser = "root";
	String conPsw = "1";
	String station_total[];
	Transfer()
	{
		ConnectDatabase();
	}
   private void ConnectDatabase() 
   {
   	try {
   		Class.forName("com.mysql.jdbc.Driver");
   		conn = DriverManager.getConnection(
   				"jdbc:mysql://localhost:3306/busline",// 一会在改吧,名字肯定不能是它~
   				conUser, conPsw);
   		if(!conn.isClosed())
   			System.out.println("Succeeded connecting to the Database!");
   	     }
   	catch(ClassNotFoundException e){
   		System.out.println("Sorry,can`t find the Driver!");
   		e.printStackTrace();
	                                   } 
   	catch(SQLException e){
   		e.printStackTrace();
	                         } 
   	catch(Exception e){
   		e.printStackTrace();
	                      }
    }
   private String directarrive(String start,String end,String city)
   {
   	String result=new String();
   	
   	try
   	{
   		String sql="SELECT A.BUS_NO FROM  (SELECT BUS_NO FROM bus_line WHERE STATION = '"+start+"'and CITY='"+city+"') A, (SELECT BUS_NO FROM bus_line WHERE STATION = '"+end+"'and CITY='"+city+"') B WHERE A.BUS_NO = B.BUS_NO ;";
   		stm=conn.createStatement();
   		rs=stm.executeQuery(sql);
   		while(rs.next())
   		{
   			String bn=rs.getString("BUS_NO");
   			result+=bn+"、";
   		}
   	}
   	catch(Exception e)
   	{}
   	return result;
   	
   }
   private String onetimechange(String start,String end,String city)
   {
   	String result=new String();
   	String line1=new String(),line2=new String();
   	try
   	{
   		String sql="SELECT A.STATION FROM ( SELECT DISTINCT STATION,RANK FROM bus_line WHERE BUS_NO IN (SELECT BUS_NO FROM bus_line WHERE STATION = '"+start+"')) AS A,(SELECT DISTINCT STATION,RANK FROM bus_line WHERE BUS_NO IN  (SELECT BUS_NO FROM bus_line WHERE STATION = '"+end+"'))AS B  WHERE A.STATION= B.STATION;";
   		//System.out.println(sql);
   		stm=conn.createStatement();
   		rs=stm.executeQuery(sql);
   		while(rs.next())
   		{
   			String st=rs.getString("STATION");
   			line1=directarrive(start,st,city);
   			line2=directarrive(st,end,city);
   			result+="搭乘"+line1.substring(0, line1.length()-1)+"在"+st+"转乘"+line2.substring(0, line2.length()-1)+"到达";
   		}
   		if(result.equals(""))
   		{
   			return "从"+start+"至"+end+"无法通过一次转车到达";
   		}
   		result="从"+start+"到"+end+"可通过：\n"+result+"转乘一次到达";
   	}
   	catch(Exception e)
   	{}
   	return result;
   }
   String busStationToStation(String start, String end,String city) 	
   {
	
   	String result=new String();
   	String line=new String();
   	line=directarrive(start, end,"西安");
   	if(!line.equals(""))
   	{
   		result="从"+start+"到"+end+"可搭乘"+line+"直达";
   		return result;
   	}
   	else
   	{
   		result="从"+start+"到"+end+"无法直达。\n";
   		result+=onetimechange(start,end,city);
   	}
   	return result;
   }
}
class MTbusline 
{
	static void UPdate_city_bus()
	{
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/busline";
		String user = "root";
		String password = "123456";
		String rst=null;
		String sql;
		ResultSet rs = null;
		try 
		{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			if(!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			Statement statement = conn.createStatement(),state=conn.createStatement();
			System.out.println("-----------------");
			System.out.println("执行结果如下所示:");
			System.out.println("-----------------");
			sql = "select distinct CITY,BUS_NO from bus_line;";
			rs = statement.executeQuery(sql);
			System.out.println("CITY"+"\t"+"BUS_NO");
			System.out.println("-----------------");
			state.execute("delete from city_bus");
			while(rs.next())
			{
				String bn=rs.getString("BUS_NO");
				String cy=rs.getString("CITY");
				String temp=cy+"\t"+bn;
				System.out.println(temp);
				String temp_sql="insert into city_bus value('"+cy+"','"+bn+"');";
				state.execute(temp_sql);
			}
			rs.close();	
			conn.close();
		}
		catch(Exception e)
		{}
	}
	static void UPdate_user_city(String User,String City)
	{
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/busline";
		String user="root";
		String password = "1";
		try
		{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			if(!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			Statement statement = conn.createStatement();
			String sql="select CITY from user_city where user='"+User+"';";
			ResultSet rst=statement.executeQuery(sql);
			if(rst.next())
			{
				sql="update user_city set CITY='"+City+"' where USER='"+User+"';";
				statement.execute(sql);
			}
			else
			{
				sql="insert into user_city value('"+User+"','"+City+"');";
				statement.execute(sql);
			}
		}
		catch(Exception e)
		{}
	}
	static void create_city_Table()
	{
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/busline";
		String user="root";
		String password = "1";
		int i=0;
		try
		{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			if(!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			Statement statement = conn.createStatement();
			//Statement state=conn.createStatement();
			String sql="select distinct CITY from city_bus;";
			ResultSet rst=statement.executeQuery(sql);
			while(rst.next())
			{
				String city=rst.getString("CITY");
				System.out.println(city);
				Class.forName(driver);
				Connection conn1 = DriverManager.getConnection(url, user, password);
				Statement statement1 = conn1.createStatement();
				String temp_sql="create table bus_in_"+(i++)+"(CITY varchar(40),BUS_NO varchar(40));";
				
				ResultSet rst0 = statement1.executeQuery(temp_sql);
				
				String SQL="select BUS_NO from city_bus where CITY='"+city+"';";
				conn=DriverManager.getConnection(url, user, password);
				statement=conn.createStatement();
				if(!conn.isClosed()) System.out.println("yes");
				ResultSet rst1=statement.executeQuery(SQL);
				while(rst1.next())
				{
					String bn=rst1.getString("BUS_NO");
					sql="insert into bus_in_"+(i-1)+" value('"+city+"','"+bn+"');";
					statement.execute(sql);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Error");
		}
	}
}



class split {
	public split(String str,String[] args) {

		StringTokenizer token = new StringTokenizer(str, "@");//按照空格和逗号进行截取 
		//String[] array = new String[2];//定义一个字符串数组 
		int i = 0; 
		while (token.hasMoreTokens()) { 
		args[i] = token.nextToken();//将分割开的子字符串放入数组中 
		i++; 
		} 
		for (int j = 0; j < args.length; j++) { 
		System.out.println(args[j] + " ");//遍历输出数组 
		} 
	}
}
