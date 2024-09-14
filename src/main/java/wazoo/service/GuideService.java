package wazoo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wazoo.dto.GuideDto;
import wazoo.entity.Guide;
import wazoo.entity.User;
import wazoo.repository.GuideRepository;
import wazoo.repository.UserRepository;
import wazoo.utils.S3Uploader;

import java.sql.Date;

@Service
public class GuideService {

    @Autowired
    private GuideRepository guideRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private S3Uploader s3Uploader;

    public boolean registerGuide(MultipartFile file, GuideDto guideDto) {
        try {
            Guide guide = convertToGuideEntity(guideDto);

            // userNo를 이용해 User 엔티티 조회
            User user = userRepository.findByUserNo(guideDto.getUserNo());
            // if user == null
            guide.setUser(user);

            // S3Uploader 를 사용하여 파일 업로드
            String fileUrl = s3Uploader.upload(file, "guides");
            guide.setProfile(fileUrl);

            guideRepository.save(guide);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateGuide(Integer guideId, MultipartFile file, GuideDto guideDto) {
        try {
            Guide existingGuide = guideRepository.findByGuideId(guideId);
            // if existingGuide == null
            Guide updatedGuide = convertToGuideEntity(guideDto);

            // userNo를 이용해 User 엔티티 조회
            User user = userRepository.findByUserNo(guideDto.getUserNo());
            // if user == null
            updatedGuide.setUser(user);

            if (!file.isEmpty()) {
                String fileUrl = s3Uploader.updateFile(file, existingGuide.getProfile(), "guides");
                updatedGuide.setProfile(fileUrl);
            }

            existingGuide.setIntroduction(updatedGuide.getIntroduction());
            existingGuide.setStartDate(updatedGuide.getStartDate());
            existingGuide.setEndDate(updatedGuide.getEndDate());
            existingGuide.setActiveArea(updatedGuide.getActiveArea());
            existingGuide.setLatitude(updatedGuide.getLatitude());
            existingGuide.setLongitude(updatedGuide.getLongitude());
            existingGuide.setGuidePrice(updatedGuide.getGuidePrice());
            existingGuide.setGuideTravelType(updatedGuide.getGuideTravelType());
            existingGuide.setProfile(updatedGuide.getProfile());
            existingGuide.setUser(user);

            guideRepository.save(existingGuide);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Guide convertToGuideEntity(GuideDto guideDto) {
        Guide guide = new Guide();
        guide.setIntroduction(guideDto.getIntroduction());
        guide.setActiveArea(guideDto.getActiveArea());
        guide.setLatitude(guideDto.getLatitude());
        guide.setLongitude(guideDto.getLongitude());
        guide.setGuidePrice(guideDto.getGuidePrice());
        guide.setStartDate(Date.valueOf(guideDto.getStartDate()));
        guide.setEndDate(Date.valueOf(guideDto.getEndDate()));
        guide.setGuideTravelType(guideDto.getGuideTravelType());

        return guide;
    }
}
