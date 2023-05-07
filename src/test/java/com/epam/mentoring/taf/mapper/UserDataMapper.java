package com.epam.mentoring.taf.mapper;

import com.epam.mentoring.taf.dataobject.ApiUserDTO;
import com.epam.mentoring.taf.dataobject.UserDataDTO;
import com.epam.mentoring.taf.model.UserDataModel;

public final class UserDataMapper {

    private UserDataMapper() {
    }

    public static ApiUserDTO mapToDTO(UserDataModel model) {
        return new ApiUserDTO
                .ApiUserDTOBuilder(model.getUserEmail(), model.getUserPassword())
                .setUsername(model.getUserName())
                .build();
    }

    public static ApiUserDTO mapToDTO(UserDataDTO dto) {
        return new ApiUserDTO
                .ApiUserDTOBuilder(dto.getUserEmail(), dto.getUserPassword())
                .setUsername(dto.getUserName())
                .build();
    }
}
