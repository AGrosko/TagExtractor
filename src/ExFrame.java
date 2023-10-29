import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

public class ExFrame extends JFrame{

    Map<String, Integer> wordMap;
    Set<String> stopSet;

    JPanel mainPnl;
    JPanel chooserPnl;
        JFileChooser bookChooser;
            File bookFile;
        JFileChooser stopChooser;
            File stopFile;

        JButton runButton;
    JPanel outPnl;
        JTextArea outTA;
        JScrollPane outScroll;
        JButton saveBtn;


    public ExFrame() {

        wordMap = new HashMap<String, Integer>();
        stopSet = new HashSet<String>();


        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());

        addChooserPnl();
        addOutPnl();



        this.add(mainPnl);



    }

    public void addChooserPnl(){
        File workingDirectory = new File(System.getProperty("user.dir"));
        chooserPnl = new JPanel();
        bookChooser = new JFileChooser();
        stopChooser = new JFileChooser();
        chooserPnl.add(bookChooser);
        bookChooser.setCurrentDirectory(workingDirectory);
        chooserPnl.add(stopChooser);
        stopChooser.setCurrentDirectory(workingDirectory);

        runButton = new JButton("Run");

        chooserPnl.add(runButton);

        mainPnl.add(chooserPnl,BorderLayout.NORTH);

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookFile = bookChooser.getSelectedFile();
                stopFile = stopChooser.getSelectedFile();
                String temp;
                outTA.append("Reading file: " + bookFile.getName() + "\n");

                try {
                    Scanner stopSc = new Scanner(stopFile);
                    Scanner bookSc = new Scanner(bookFile);

                    while (stopSc.hasNextLine()){
                        stopSet.add(stopSc.nextLine());

                    }


                    while (bookSc.hasNext()){
                        temp = bookSc.next();
                        temp = temp.toLowerCase(Locale.ROOT);
                        temp = temp.replaceAll("\\p{P}","");

                        if(!(stopSet.contains(temp)) && !(temp.isEmpty())){
                            if(wordMap.containsKey(temp)){wordMap.put(temp,wordMap.get(temp)+1);}
                            else{
                            wordMap.put(temp,1);}
                        }
                    }

                    while(!wordMap.isEmpty()){
                    outTA.append(getMaxKey());
                    outTA.append("\n");

                    }

                    saveBtn.setVisible(true);


                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });


    }
    public void addOutPnl(){
        outPnl = new JPanel();
        outTA = new JTextArea(10,45);
        outScroll = new JScrollPane(outTA);

        saveBtn = new JButton("Save to File");
        saveBtn.setVisible(false);


        outPnl.setBorder(BorderFactory.createEmptyBorder());
        outPnl.add(outScroll,BorderLayout.CENTER);
        outPnl.add(saveBtn,BorderLayout.SOUTH);

        mainPnl.add(outPnl,BorderLayout.CENTER);

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File outFile = new File(bookFile.getName() + "Freq");
                try {
                    FileWriter outWriter = new FileWriter(outFile.getName());
                    PrintWriter outPrint = new PrintWriter(outWriter);

                    outPrint.print(outTA.getText());

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

    }

    public String getMaxKey(){

        String currMax = "";
        for (Map.Entry<String,Integer> entry : wordMap.entrySet()){
            if (currMax.isEmpty()){currMax = entry.getKey();}
            if (entry.getValue() > wordMap.get(currMax)){
                currMax = entry.getKey();
            }


        }

        String temp = currMax;
        currMax += (" = " + wordMap.get(currMax));
        wordMap.remove(temp);
        return currMax;
    }




}
