/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general.reposes;

import org.springframework.data.repository.CrudRepository;

import general.entities.SchoolClass;
import general.entities.Task;

/**
 *
 * @author dmali
 */
public interface TaskRepos extends CrudRepository<Task, Long> {
    Iterable<Task> findTasksBySchoolClass(SchoolClass schoolClass);

    public Task findTaskById(Long id);
}
