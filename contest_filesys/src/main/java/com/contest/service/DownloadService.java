package com.contest.service;

import com.contest.dto.user.UserDto;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

public interface DownloadService {
    public void download(String fileId, HttpServletResponse response, UserDto userDto, boolean isOnline);
}
