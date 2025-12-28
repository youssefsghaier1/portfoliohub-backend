package com.portfoliohub.backend.controller;
import com.portfoliohub.backend.dto.portfolio.PublicPortfolioResponseDto;
import com.portfoliohub.backend.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/portfolio")
@RequiredArgsConstructor
public class PublicPortfolioController {

    private final PortfolioService portfolioService;

    // 🌍 PUBLIC PORTFOLIO
    @GetMapping("/{username}")
    public ResponseEntity<PublicPortfolioResponseDto> getPortfolio(
            @PathVariable String username
    ) {
        return ResponseEntity.ok(
                portfolioService.getPublicPortfolio(username)
        );
    }

}
