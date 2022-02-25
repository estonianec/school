package ru.hogwarts.school.service.impl;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

class AvatarServiceImplTest {

    @Test
    void uploadAvatar() {
        File dir = new File("avatars"); //path указывает на директорию
        String[] arrFiles = dir.list();
        assert arrFiles != null;
        List<String> lst = Arrays.asList(arrFiles);
        System.out.println(lst);
    }

    @Test
    void findAvatar() throws IOException {
        int id = 4;
        File f=new File("avatars");
        String[] arr=f.list((f1, name) -> name.startsWith(id + "."));
        assert arr != null;
        for(String x:arr)
        {
            Path previousAvatarPath = Path.of("avatars", x);
            System.out.println(x);
            Files.delete(previousAvatarPath);
        }

    }
}