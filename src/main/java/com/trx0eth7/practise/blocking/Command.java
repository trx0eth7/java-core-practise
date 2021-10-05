package com.trx0eth7.practise.blocking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vasilev
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Command {
    private CommandType type;
    private String args;
}
