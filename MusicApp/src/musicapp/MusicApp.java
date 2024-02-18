/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package musicapp;

/**
 *
 * @author Clinton
 */



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MusicApp {
    private JFrame frame;
    private JTextField titleField, artistField, moodField, bpmField, keyField, durationField;
    private JTextArea resultArea;
    private List<Song> songs;

    public MusicApp() {
        songs = new ArrayList<>();
        loadSongs(); // Load songs from file when application starts
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Music App");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 2));

        titleField = new JTextField();
        artistField = new JTextField(); // Added artist field
        moodField = new JTextField();
        bpmField = new JTextField();
        keyField = new JTextField();
        durationField = new JTextField();
        JButton addButton = new JButton("Add Song");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSong();
            }
        });

        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Artist:")); // Added artist label
        inputPanel.add(artistField); // Added artist field
        inputPanel.add(new JLabel("Mood:"));
        inputPanel.add(moodField);
        inputPanel.add(new JLabel("BPM:"));
        inputPanel.add(bpmField);
        inputPanel.add(new JLabel("Key:"));
        inputPanel.add(keyField);
        inputPanel.add(new JLabel("Duration:"));
        inputPanel.add(durationField);
        inputPanel.add(addButton);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(2, 2));

        JButton searchTitleButton = new JButton("Search by Title");
        searchTitleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchByTitle();
            }
        });
        JButton searchBpmButton = new JButton("Search by BPM");
        searchBpmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchByBPM();
            }
        });
        JButton searchMoodButton = new JButton("Search by Mood");
        searchMoodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchByMood();
            }
        });
        JButton deleteLastButton = new JButton("Delete Last Added");
deleteLastButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        deleteLastAdded();
    }
});
searchPanel.add(deleteLastButton);

        JButton viewAllButton = new JButton("View All");
        viewAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAllSongs();
            }
        });

        searchPanel.add(searchTitleButton);
        searchPanel.add(searchBpmButton);
        searchPanel.add(searchMoodButton);
        searchPanel.add(viewAllButton);

        resultArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(resultArea);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(searchPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void addSong() {
        String title = titleField.getText();
        String artist = artistField.getText(); // Retrieve artist name
        String mood = moodField.getText();
        int bpm = Integer.parseInt(bpmField.getText());
        String key = keyField.getText();
        String duration = durationField.getText();
        Song song = new Song(title, artist, mood, bpm, key, duration); // Update constructor
        songs.add(song);
        titleField.setText("");
        artistField.setText(""); // Clear artist field
        moodField.setText("");
        bpmField.setText("");
        keyField.setText("");
        durationField.setText("");
        JOptionPane.showMessageDialog(frame, "Song added successfully.");
        saveSongs(); // Save songs to file when a new song is added
    }

    private void searchByTitle() {
        String title = JOptionPane.showInputDialog(frame, "Enter title to search:");
        List<Song> foundSongs = new ArrayList<>();
        for (Song song : songs) {
            if (song.getTitle().equalsIgnoreCase(title)) {
                foundSongs.add(song);
            }
        }
        displaySearchResult(foundSongs);
    }

    private void searchByBPM() {
        int bpm = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter BPM to search:"));
        List<Song> foundSongs = new ArrayList<>();
        for (Song song : songs) {
            if (song.getBpm() == bpm) {
                foundSongs.add(song);
            }
        }
        displaySearchResult(foundSongs);
    }

    private void deleteLastAdded() {
        if (!songs.isEmpty()) {
            songs.remove(songs.size() - 1);
            saveSongs(); // Save songs after deletion
            JOptionPane.showMessageDialog(frame, "Last added song deleted.");
        } else {
            JOptionPane.showMessageDialog(frame, "No songs to delete.");
        }
    }

    private void searchByMood() {
        String mood = JOptionPane.showInputDialog(frame, "Enter mood to search:");
        List<Song> foundSongs = new ArrayList<>();
        for (Song song : songs) {
            if (song.getMood().equalsIgnoreCase(mood)) {
                foundSongs.add(song);
            }
        }
        displaySearchResult(foundSongs);
    }

    private void viewAllSongs() {
        displaySearchResult(songs);
    }

    private void displaySearchResult(List<Song> foundSongs) {
        resultArea.setText("");
        if (foundSongs.isEmpty()) {
            resultArea.append("No songs found.");
        } else {
            for (Song song : foundSongs) {
                resultArea.append(song.toString() + "\n");
            }
        }
    }

    private void saveSongs() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("songs.txt"))) {
            for (Song song : songs) {
                writer.println(song.getTitle() + "," + song.getArtist() + "," + song.getMood() + "," + song.getBpm() + "," + song.getKey() + "," + song.getDuration());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSongs() {
        try (BufferedReader reader = new BufferedReader(new FileReader("songs.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) { // Updated to account for artist
                    String title = parts[0];
                    String artist = parts[1]; // Added artist
                    String mood = parts[2];
                    int bpm = Integer.parseInt(parts[3]);
                    String key = parts[4];
                    String duration = parts[5];
                    Song song = new Song(title, artist, mood, bpm, key, duration); // Updated constructor
                    songs.add(song);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MusicApp();
            }
        });
    }

    static class Song {
        private String title;
        private String artist; // Added artist field
        private String mood;
        private int bpm;
        private String key;
        private String duration;

        public Song(String title, String artist, String mood, int bpm, String key, String duration) {
            this.title = title;
            this.artist = artist; // Updated constructor
            this.mood = mood;
            this.bpm = bpm;
            this.key = key;
            this.duration = duration;
        }

        public String getTitle() {
            return title;
        }

        public String getArtist() {
            return artist; // Added getter for artist
        }

        public String getMood() {
            return mood;
        }

        public int getBpm() {
            return bpm;
        }

        public String getKey() {
            return key;
        }

        public String getDuration() {
            return duration;
        }

        @Override
        public String toString() {
            return "Song{" +
                    "title='" + title + '\'' +
                    ", artist='" + artist + '\'' +
                    ", mood='" + mood + '\'' +
                    ", bpm=" + bpm +
                    ", key='" + key + '\'' +
                    ", duration='" + duration + '\'' +
                    '}';
        }
    }
}
