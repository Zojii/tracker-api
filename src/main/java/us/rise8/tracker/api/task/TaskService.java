package us.rise8.tracker.api.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import us.rise8.tracker.api.AbstractCRUDService;
import us.rise8.tracker.api.task.dto.CreateTaskDTO;
import us.rise8.tracker.api.task.dto.TaskDTO;
import us.rise8.tracker.api.task.dto.UpdateTaskDTO;
import us.rise8.tracker.config.CustomProperty;

@Service
public class TaskService extends AbstractCRUDService<Task, TaskDTO, TaskRepository> {

    CustomProperty property;

    @Autowired
    public TaskService(TaskRepository repository, CustomProperty property) {
        super(repository, Task.class, TaskDTO.class);
        this.property = property;
    }

    public Task create(Long id, CreateTaskDTO createTaskDTO) {
        Task task = getObject(id);
        task.setDetail(createTaskDTO.getDetail());
        task.setComplete(createTaskDTO.isComplete());

        return repository.save(task);
    }

    public Task updateById(Long id, UpdateTaskDTO updateTaskDTO) {
        Task task = getObject(id);
        task.setDetail(updateTaskDTO.getDetail());
        task.setComplete(updateTaskDTO.isComplete());

        return repository.save(task);
    }

}
