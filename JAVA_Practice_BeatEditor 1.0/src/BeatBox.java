import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by defiance on 2017/7/7.
 */
public class BeatBox {
    JPanel mainPanel;
    ArrayList<JCheckBox> checkboxList;
    Sequencer sequencer;
    Sequence seq;
    Track track;
    JFrame subFrame;

    //for the instruments
    String[] instrumentsNames={"Bass Drum","Closed Hi-Hat","Open Hi-Hat","Acoustic Snarl"};
    int[] instrumentsID={35,42,46,38};

    public class MyStartListener implements ActionListener{
        public void actionPerformed(ActionEvent click)
        {
            buildTrackAndStart();
        }
    }

    public void buildTrackAndStart()
    {
        int[] trackList=null;

        seq.deleteTrack(track);
        track=seq.createTrack();

        for(int i=0;i<4;i++)
        {
            trackList=new int[16];

            int key=instrumentsID[i];

            for(int j=0;j<16;j++)
            {
                JCheckBox boxes=(JCheckBox) checkboxList.get(j+(16*i));
                if(boxes.isSelected())
                {
                    trackList[j]=key;
                }
                else
                {
                    trackList[j]=0;
                }
            }

            makeTrack(trackList);
            track.add(makeEvent(176,1,127,0,16));
        }

        track.add(makeEvent(192,2,1,0,15));
        try
        {
            sequencer.setSequence(seq);
            sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);
            sequencer.start();
            sequencer.setTempoInBPM(120);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public MidiEvent makeEvent(int comd,int chan,int a1,int a2,int tick)
    {
        MidiEvent event=null;
        try{
            ShortMessage msg=new ShortMessage();
            msg.setMessage(comd,chan,a1,a2);
            event=new MidiEvent(msg,tick);
        }catch(Exception e){
            e.printStackTrace();
        }
        return event;
    }

    public void makeTrack(int[] trackList)
    {
        for (int i=0;i<16;i++)
        {
            int key=trackList[i];

            if(key!=0)
            {
                track.add(makeEvent(144,9,key,100,i));
                track.add(makeEvent(144,9,key,100,i));
            }
        }
    }

    public class MyStopListener implements ActionListener{
        public void actionPerformed(ActionEvent click)
        {
            sequencer.stop();
        }
    }

    public class MyTempoUpListener implements ActionListener{
        public void actionPerformed(ActionEvent click)
        {
            float tempoFactor=sequencer.getTempoFactor();
            sequencer.setTempoFactor((float)(tempoFactor*2));
        }
    }

    public class MyTempoDownListener implements ActionListener{
        public void actionPerformed(ActionEvent click)
        {
            float tempoFactor=sequencer.getTempoFactor();
            sequencer.setTempoFactor((float)(tempoFactor/2));
        }
    }

    public void setupGUI()
    {
        subFrame=new JFrame("WangWang Beatbox 1.0");
        subFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout=new BorderLayout();
        JPanel background=new JPanel(layout);
        background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        checkboxList=new ArrayList<JCheckBox>();
        Box buttonBox=new Box(BoxLayout.Y_AXIS);//GUI default settings for check boxes;

        JButton startButton=new JButton("start");
        startButton.addActionListener(new MyStartListener());
        buttonBox.add(startButton);

        JButton stopButton=new JButton("stop");
        stopButton.addActionListener(new MyStopListener());
        buttonBox.add(stopButton);

        JButton tempoUpButton=new JButton("tempo up");
        stopButton.addActionListener(new MyTempoUpListener());
        buttonBox.add(tempoUpButton);

        JButton tempoDownButton=new JButton("tempo down");
        stopButton.addActionListener(new MyTempoDownListener());
        buttonBox.add(tempoDownButton);

        Box nameBox=new Box(BoxLayout.Y_AXIS);
        for(int i=0;i<4;i++){
            nameBox.add(new Label(instrumentsNames[i]));
        }

        background.add(BorderLayout.EAST,buttonBox);
        background.add(BorderLayout.WEST,nameBox);

        subFrame.getContentPane().add(background);

        GridLayout grid=new GridLayout(4,16);
        grid.setVgap(1);
        grid.setHgap(2);
        mainPanel=new JPanel(grid);
        background.add(BorderLayout.CENTER,mainPanel);

        for(int i=0;i<64;i++)
        {
            JCheckBox c=new JCheckBox();
            c.setSelected(false);
            checkboxList.add(c);
            mainPanel.add(c);
        }

        setUpMidi();

        subFrame.setBounds(50,50,300,300);
        subFrame.pack();
        subFrame.setVisible(true);
    }//the end of method setUpGUI;

    public void setUpMidi()
    {
        try{
            sequencer=MidiSystem.getSequencer();
            sequencer.open();
            seq=new Sequence(Sequence.PPQ,4);
            track=seq.createTrack();
            sequencer.setTempoInBPM(120);
        }catch(Exception e)
        {
            //do nothing;
        }
    }


    public void run()
    {
        setupGUI();
    }

}
