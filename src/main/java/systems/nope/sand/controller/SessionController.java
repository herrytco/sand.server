package systems.nope.sand.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import systems.nope.sand.model.Session;
import systems.nope.sand.model.request.SessionAddRequest;
import systems.nope.sand.repository.SessionRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private SessionRepository sessionRepository;

    public SessionController(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @GetMapping
    public List<Session> all() {
        return sessionRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> add(
            @RequestBody SessionAddRequest request
    ) {
        Optional<Session> possibleExistingSession = sessionRepository.findByName(request.getName());

        if (possibleExistingSession.isPresent())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        Session sessionNew = new Session(request.getName(), request.getDescription());
        sessionRepository.save(sessionNew);

        return ResponseEntity.ok().build();
    }
}
