import javax.sound.midi.*;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
import java.awt.*;


public class MiniMiniMusicApp
{
    static  JFrame f=new JFrame("Wangwang's music app");//the GUI frame and the title;
    static MyDrawPanel drawpanel;

    public static void main( String[] args )
    {
        MiniMiniMusicApp mini=new MiniMiniMusicApp();
        mini.play();
    }

    public void setGUI()
    {
        drawpanel=new MyDrawPanel();
        f.setContentPane(drawpanel);
        f.setBounds(30,30,300,300);
        f.setVisible(true);
    }

    class MyDrawPanel extends JPanel implements ControllerEventListener
    {
        boolean msg=false;
        int count_x=0,count_y=0;

        public void controlChange(ShortMessage event)
        {
            msg=true;
            repaint();
        }

        public void paintComponent(Graphics g)
        {
            if(msg)
            {
                Graphics2D g2d=(Graphics2D) g;

                int r=(int)(count_x*5);
                int gr=(int)(0);
                int b=(int)(count_x*5);

                g.setColor(new Color(r,gr,b));

                int ht=(int)40;
                int wid=(int)40;

                int x=(int)((count_x*40)%200)+10;
                int y=(int)((count_y/5*40)%200)+10;
                count_x++;count_y++;
                g.fillRect(x,y,ht,wid);
                msg=false;
            }
        }
    }

    public void play()
    {
        setGUI();

        try{
            Sequencer player=MidiSystem.getSequencer();
            player.open();
            player.addControllerEventListener(drawpanel,new int[]{127});
            Sequence seq=new Sequence(Sequence.PPQ,4);
            Track track=seq.createTrack();

            int r=0;
            for(int i=0;i<=50;i+=1)
            {
                r=(int)i+50;
                track.add(makeEvent(144,1,r,100,i));
                track.add(makeEvent(176,1,127,0,i));
                track.add(makeEvent(128,1,r,100,i+2));
            }

            player.setSequence(seq);
            player.setTempoInBPM(120);
            player.start();

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public MidiEvent makeEvent(int comd , int chan , int a1 , int a2 , int tick)
    {
        MidiEvent event=null;
        try{
            ShortMessage a=new ShortMessage();
            a.setMessage(comd,chan,a1 ,a2);
            event=new MidiEvent(a,tick);
        }catch(Exception e){
            //do nothing
        }
        return event;
    }
}
