package ru.hogwarts.school.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarServiceImpl implements AvatarService {

    @Value("${avatars.dir.path}")
    private String avatarsDir;
    private final StudentService studentService;
    private final AvatarRepository avatarRepository;

    public AvatarServiceImpl(StudentService studentService, AvatarRepository avatarRepository) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
    }

    public void deletePreviousAvatar(long id) throws IOException {
        File f=new File(avatarsDir);
        String[] arr=f.list((f1, name) -> name.startsWith(id + "."));
        assert arr != null;
        for(String x:arr)
        {
            Path previousAvatarPath = Path.of(avatarsDir, x);
            Files.delete(previousAvatarPath);
        }
    }

    @Override
    public void uploadAvatar(Long studentId, MultipartFile avatar) throws IOException {
        Student student = studentService.getStudent(studentId);
        Path avatarPath = Path.of(avatarsDir, studentId + "." + getFileExtension(Objects.requireNonNull(avatar.getOriginalFilename())));
        Files.createDirectories(avatarPath.getParent());
        //При добавлении аватарки отличного от предыдущего расширения, заметил, что сохраняются старые неиспользуемые
        //файлы (например 1.jpg, 1.jpeg, 1.png и т.д.).
        if (avatarRepository.findAvatarByStudentId(studentId).isPresent()) {
            deletePreviousAvatar(studentId);
        }

        try (
                InputStream is = avatar.getInputStream();
                OutputStream os = Files.newOutputStream(avatarPath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }

        Avatar newAvatar = findAvatar(studentId);
        newAvatar.setStudent(student);
        newAvatar.setFilePath(avatarPath.toString());
        newAvatar.setFileSize(avatar.getSize());
        newAvatar.setMediaType(avatar.getContentType());
        newAvatar.setData(avatar.getBytes());
        avatarRepository.save(newAvatar);
    }
    @Override
    public Avatar findAvatar(long id) {
        return avatarRepository.findAvatarByStudentId(id).orElse(new Avatar());
    }


    private static String getFileExtension(String originalFileName) {
        if(originalFileName.lastIndexOf(".") != -1 && originalFileName.lastIndexOf(".") != 0)
            return originalFileName.substring(originalFileName.lastIndexOf(".")+1);
        else return "";
    }
}
