package online.stemlink.skillmentor.services;

import online.stemlink.skillmentor.dtos.SessionDTO;
import online.stemlink.skillmentor.entities.Session;
import online.stemlink.skillmentor.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SessionService {

    Session createNewSession(SessionDTO sessionDTO);
    List<Session> getAllSessions();
    Session getSessionById(Long id);
    Session updateSessionById(Long id, SessionDTO updatedSessionDTO);
    void deleteSession(Long id);

    // Frontend enrollment flow — student is resolved from the Clerk JWT
    Session enrollSession(UserPrincipal userPrincipal, SessionDTO sessionDTO);
    List<Session> getSessionsByStudentEmail(String email);
}
