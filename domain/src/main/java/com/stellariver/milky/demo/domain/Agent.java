package com.stellariver.milky.demo.domain;

import com.stellariver.milky.demo.domain.command.AgentCommand;
import com.stellariver.milky.demo.domain.event.AgentEvent;
import com.stellariver.milky.domain.support.base.AggregateRoot;
import com.stellariver.milky.domain.support.command.ConstructorHandler;
import com.stellariver.milky.domain.support.context.Context;
import lombok.Data;

import java.util.List;

@Data
public class Agent extends AggregateRoot {
    Long agentId;
    Long roundId;
    Long compId;
    Long userId;
    List<Long> metaUnitIds;
    @ConstructorHandler
    static public Agent create(AgentCommand.Create create, Context context) {
        Agent agent = new Agent();
        agent.setAgentId(create.getAgentId());
        agent.setCompId(create.getCompId());
        agent.setRoundId(create.getRoundId());
        agent.setUserId(create.getUserId());
        agent.setMetaUnitIds(create.getMetaUnitIds());
        AgentEvent.Created event = AgentEvent.Created.builder()
                .agentId(agent.getAgentId())
                .agent(agent)
                .build();
        context.publish(event);
        return agent;
    }

    @Override
    public String getAggregateId() {
        return agentId.toString();
    }

}
